package com.jinelei.iotgenius.auth.client.configuration.authentication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.*;
import org.springframework.util.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinelei.iotgenius.auth.dto.client.ClientResponse;
import com.jinelei.iotgenius.auth.dto.permission.PermissionResponse;
import com.jinelei.iotgenius.auth.property.AuthorizationProperty;
import com.jinelei.iotgenius.common.wrapper.RepeatRequestWrapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@SuppressWarnings("unused")
public class ClientAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final Logger log = LoggerFactory.getLogger(ClientAuthenticationFilter.class);
    private final static Function<AuthorizationProperty, RequestMatcher> REQUEST_MATCHER_FUNCTION = property -> {
        List<RequestMatcher> list = new ArrayList<>();
        property.getIgnoreUrls().stream().map(AntPathRequestMatcher::new).forEach(list::add);
        Optional.ofNullable(property.getLoginUrl()).map(AntPathRequestMatcher::new).ifPresent(list::add);
        OrRequestMatcher or = new OrRequestMatcher(list);
        NegatedRequestMatcher not = new NegatedRequestMatcher(or);
        RequestHeaderRequestMatcher header = new RequestHeaderRequestMatcher(property.getAccessKeyHeader());
        return new AndRequestMatcher(not, header);
    };
    private final AuthorizationProperty property;
    private final ObjectMapper objectMapper;
    private final AuthenticationManager authenticationManager;
    private final ClientDetailService clientDetailService;

    public ClientAuthenticationFilter(AuthorizationProperty property, ObjectMapper objectMapper,
            ClientDetailService clientDetailService, AuthenticationManager authenticationManager) {
        super(REQUEST_MATCHER_FUNCTION.apply(property), authenticationManager);
        this.property = property;
        this.objectMapper = objectMapper;
        this.authenticationManager = authenticationManager;
        this.clientDetailService = clientDetailService;
    }

    protected Map<String, String> obtainParams(HttpServletRequest request) {
        final HttpMethod method = HttpMethod.valueOf(request.getMethod());
        final MediaType contentType = MediaType.parseMediaType(request.getContentType());
        final Map<String, String> params = new HashMap<>();
        if (HttpMethod.POST.equals(method) && MediaType.APPLICATION_JSON.equals(contentType)) {
            if (request instanceof RepeatRequestWrapper wrapper) {
                try {
                    Map<String, String> map = objectMapper.readValue(wrapper.getBody(),
                            new TypeReference<>() {
                            });
                    params.putAll(map);
                } catch (IOException e) {
                    log.error("登陆失败: body参数不合法");
                }
            }
        }
        // 当请求方法是GET时，检查是否具有query参数
        if (HttpMethod.GET.equals(method)) {
            request.getParameterMap().forEach((key, value) -> {
                if (value.length != 0) {
                    params.put(key, value[0]);
                }
            });
        }
        params.putIfAbsent(property.getSignatureHeader(), obtainSignature(request));
        params.putIfAbsent(property.getTimestampHeader(), obtainTimestamp(request));
        return params;
    }

    protected String obtainSignature(HttpServletRequest request) {
        return Optional.ofNullable(property)
                .map(AuthorizationProperty::getSignatureHeader)
                .map(request::getHeader)
                .filter(s -> !s.isEmpty())
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("signature不能为空"));
    }

    protected String obtainTimestamp(HttpServletRequest request) {
        return Optional.ofNullable(property)
                .map(AuthorizationProperty::getTimestampHeader)
                .map(request::getHeader)
                .filter(s -> !s.isEmpty())
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("timestamp不能为空"));
    }

    protected String obtainAccessKey(HttpServletRequest request) {
        return Optional.ofNullable(property)
                .map(AuthorizationProperty::getAccessKeyHeader)
                .map(request::getHeader)
                .filter(s -> !s.isEmpty())
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("accessKey不能为空"));
    }

    protected String obtainSecretKey(ClientResponse client) throws AuthenticationCredentialsNotFoundException {
        return Optional.ofNullable(client)
                .map(ClientResponse::getSecretKey)
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
            throws AuthenticationException {
        final String accessKey = obtainAccessKey(request);
        final ClientResponse result = obtainClient(accessKey);
        ClientAuthenticationToken authenticationToken = new ClientAuthenticationToken(accessKey,
                obtainSecretKey(result), obtainSignature(request),
                obtainTimestamp(request), obtainParams(request),
                obtainAuthorities(result));
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }
}