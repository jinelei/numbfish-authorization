package com.jinelei.iotgenius.auth.authentication;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinelei.iotgenius.auth.dto.client.ClientResponse;
import com.jinelei.iotgenius.auth.dto.permission.PermissionResponse;
import com.jinelei.iotgenius.auth.property.AuthorizationProperty;
import com.jinelei.iotgenius.common.wrapper.RepeatRequestWrapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ClientAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final Logger log = LoggerFactory.getLogger(ClientAuthenticationFilter.class);
    private final static Function<List<String>, RequestMatcher> REQUEST_MATCHER_FUCTION = (ignoreUrls) -> {
        List<RequestMatcher> list = ignoreUrls.stream().map(AntPathRequestMatcher::new).collect(Collectors.toList());
        OrRequestMatcher or = new OrRequestMatcher(list);
        return new NegatedRequestMatcher(or);
    };
    private final AuthorizationProperty property;
    private final ObjectMapper objectMapper;
    private final AuthenticationManager authenticationManager;
    private final ClientDetailService clientDetailService;

    public ClientAuthenticationFilter(AuthorizationProperty property, ObjectMapper objectMapper,
            ClientDetailService clientDetailService, AuthenticationManager authenticationManager) {
        super(REQUEST_MATCHER_FUCTION.apply(property.getIgnoreUrls()), authenticationManager);
        this.property = property;
        this.objectMapper = objectMapper;
        this.authenticationManager = authenticationManager;
        this.clientDetailService = clientDetailService;
    }

    protected Map<String, String> obtainParams(HttpServletRequest request) {
        final HttpMethod method = HttpMethod.valueOf(request.getMethod());
        final MediaType contentType = MediaType.parseMediaType(request.getContentType());
        final Map<String, String> params = new HashMap<>();
        if (request instanceof HttpServletRequest httpRequest) {
            // 当请求方法是POST时，检查Content-Type是否为application/json，检查是否具有body参数
            if (HttpMethod.POST.equals(method) && MediaType.APPLICATION_JSON.equals(contentType)) {
                if (request instanceof RepeatRequestWrapper wrapper) {
                    try {
                        Map<String, String> map = objectMapper.readValue(wrapper.getBody(),
                                new TypeReference<Map<String, String>>() {
                                });
                        params.putAll(map);
                    } catch (IOException e) {
                        log.error("登陆失败: body参数不合法");
                    }
                }
            }
            // 当请求方法是GET时，检查是否具有query参数
            if (HttpMethod.GET.equals(method)) {
                httpRequest.getParameterMap().forEach((key, value) -> {
                    if (value.length == 0) {
                        params.put(key, value[0]);
                    }
                });
            }
        }
        obtainSignature(request).ifPresent(c -> params.putIfAbsent(property.getSignatureHeader(), c));
        obtainTimestamp(request).ifPresent(c -> params.putIfAbsent(property.getTimestampHeader(), c));
        return params;
    }

    protected Optional<String> obtainSignature(HttpServletRequest request) {
        return Optional.ofNullable(property)
                .map(i -> i.getSignatureHeader())
                .map(header -> request.getHeader(header))
                .filter(s -> Objects.nonNull(s) && !s.isEmpty());
    }

    protected Optional<String> obtainTimestamp(HttpServletRequest request) {
        return Optional.ofNullable(property)
                .map(i -> i.getTimestampHeader())
                .map(header -> request.getHeader(header))
                .filter(s -> Objects.nonNull(s) && !s.isEmpty());
    }

    protected Optional<String> obtainAccessKey(HttpServletRequest request) {
        return Optional.ofNullable(property)
                .map(i -> i.getAccessKeyHeader())
                .map(header -> request.getHeader(header))
                .filter(s -> Objects.nonNull(s) && !s.isEmpty());
    }

    protected String obtainSecretKey(ClientResponse client) throws AuthenticationCredentialsNotFoundException {
        return Optional.ofNullable(client)
                .map(i -> i.getSecretKey())
                .map(String::trim)
                .filter(StringUtils::hasText)
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("未找到SecretKey"));
    }

    protected ClientResponse obtainClient(String accessKey) {
        return clientDetailService.loadClientByAccessKey(accessKey);
    }

    protected List<? extends GrantedAuthority> obtainAuthorities(ClientResponse client) {
        return client.getPermissions()
                .stream()
                .map(PermissionResponse::getCode)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        final Optional<String> accessKey = obtainAccessKey(request);
        final Optional<String> timestamp = obtainTimestamp(request);
        final Optional<String> signature = obtainSignature(request);
        if (accessKey.isEmpty() || timestamp.isEmpty() || signature.isEmpty()) {
            return null;
        }
        final ClientResponse result = obtainClient(accessKey.get());
        final Map<String, String> params = new HashMap<>();
        params.putAll(obtainParams(request));
        ClientAuthenticationToken authenticationToken = new ClientAuthenticationToken(accessKey.get(),
                obtainSecretKey(result), signature.get(), params, obtainAuthorities(result));
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }
}