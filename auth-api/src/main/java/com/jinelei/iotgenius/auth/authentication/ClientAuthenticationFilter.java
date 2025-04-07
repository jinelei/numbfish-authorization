package com.jinelei.iotgenius.auth.authentication;

import java.io.IOException;
import java.util.Collection;
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
import com.jinelei.iotgenius.auth.property.AuthorizationProperty;
import com.jinelei.iotgenius.common.wrapper.RepeatRequestWrapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ClientAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final Logger log = LoggerFactory.getLogger(ClientAuthenticationFilter.class);
    private static final Function<List<String>, RequestMatcher> REQUEST_MATCHER_FUCTION = (ignoreUrls) -> {
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

    protected String obtainSignature(HttpServletRequest request) {
        return Optional.ofNullable(property)
                .map(i -> i.getSignatureHeader())
                .map(header -> request.getHeader(header))
                .filter(s -> Objects.nonNull(s) && !s.isEmpty())
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("未找到签名"));
    }

    protected String obtainTimestamp(HttpServletRequest request) {
        return Optional.ofNullable(property)
                .map(i -> i.getTimestampHeader())
                .map(header -> request.getHeader(header))
                .filter(s -> Objects.nonNull(s) && !s.isEmpty())
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("未找到时间戳"));
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
        params.putIfAbsent(property.getSignatureHeader(), obtainSignature(request));
        params.putIfAbsent(property.getTimestampHeader(), obtainTimestamp(request));
        return params;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        final Map<String, String> params = obtainParams(request);
        final String accessKey = Optional.ofNullable(params)
                .map(i -> i.get("accessKey"))
                .filter(StringUtils::hasText)
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("未找到AccessKey"));
        final ClientResponse result = Optional.ofNullable(clientDetailService)
                .map(s -> s.loadClientByAccessKey(accessKey))
                .filter(Objects::nonNull)
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("AccessKey不存在"));
        final String secretKey = Optional.ofNullable(result)
                .map(i -> i.getSecretKey())
                .map(String::trim)
                .filter(StringUtils::hasText)
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("未找到SecretKey"));
        // 获取请求中的签名
        final String signature = obtainSignature(request);
        // 获取请求中的权限
        final List<SimpleGrantedAuthority> authorities = Optional.ofNullable(result)
                .map(i -> i.getPermissions())
                .stream()
                .flatMap(Collection::stream)
                .map(c -> c.getCode())
                .map(c -> new SimpleGrantedAuthority(c))
                .collect(Collectors.toList());
        ClientAuthenticationToken authenticationToken = new ClientAuthenticationToken(accessKey, secretKey,
                signature, params, authorities);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }
}
