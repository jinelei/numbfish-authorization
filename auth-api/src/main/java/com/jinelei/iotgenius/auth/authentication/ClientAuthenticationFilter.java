package com.jinelei.iotgenius.auth.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinelei.iotgenius.auth.dto.client.ClientResponse;
import com.jinelei.iotgenius.auth.dto.permission.PermissionResponse;
import com.jinelei.iotgenius.auth.property.AuthorizationProperty;
import com.jinelei.iotgenius.common.wrapper.RepeatRequestWrapper;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ClientAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(ClientAuthenticationFilter.class);
    private final AuthorizationProperty property;
    private final ObjectMapper objectMapper;
    private final AuthenticationManager authenticationManager;
    private final ClientDetailService clientDetailService;

    public ClientAuthenticationFilter(AuthorizationProperty property, ObjectMapper objectMapper,
            AuthenticationManager authenticationManager, ClientDetailService clientDetailService) {
        this.property = property;
        this.objectMapper = objectMapper;
        this.authenticationManager = authenticationManager;
        this.clientDetailService = clientDetailService;
    }

    protected Map<String, String> obtainParams(HttpServletRequest request) {
        final HttpMethod method = HttpMethod.valueOf(request.getMethod());
        final MediaType contentType = MediaType.parseMediaType(request.getContentType());
        final Map<String, Object> params = new HashMap<>();
        if (request instanceof HttpServletRequest httpRequest) {
            // 当请求方法是POST时，检查Content-Type是否为application/json，检查是否具有body参数
            if (HttpMethod.POST.equals(method) && MediaType.APPLICATION_JSON.equals(contentType)) {
                if (request instanceof RepeatRequestWrapper wrapper) {
                    try {
                        Map<String, Object> map = objectMapper.readValue(wrapper.getBody(),
                                new TypeReference<HashMap<String, Object>>() {
                                });
                        params.putAll(map);
                    } catch (Exception e) {
                        log.error("登陆失败: body参数不合法");
                    }
                }
            }
            // 当请求方法是GET时，检查是否具有query参数
            if (HttpMethod.GET.equals(method)) {
                params.putAll(httpRequest.getParameterMap());
            }
        }
        return new HashMap<>();
    }

    protected Optional<String> obtainSignature(HttpServletRequest request) {
        return Optional.ofNullable(property)
                .map(i -> i.getSignatureHeader())
                .map(header -> request.getHeader(header))
                .filter(s -> Objects.nonNull(s) && !s.isEmpty());
    }

    protected Optional<String> obtainTimestamp(HttpServletRequest request) {
        return Optional.empty();
    }

    protected Optional<String> obtainAccessKey(HttpServletRequest request) {
        return Optional.empty();
    }

    protected Optional<String> obtainSecretKey(HttpServletRequest request) {
        return Optional.empty();
    }

    protected ClientResponse obtainClient(String accessKey) throws AuthenticationCredentialsNotFoundException {
        final Optional.ofNullable(accessKey)
                .map(c -> clientDetailService.loadClientByAccessKey(c))
                .filter(Objects::nonNull)
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("客户端不存在"));
    }

    protected String obtainSecretKey(ClientResponse client) throws AuthenticationCredentialsNotFoundException {
        return Optional.ofNullable(client)
                .map(i -> i.getSecretKey())
                .map(String::trim)
                .filter(StringUtils::hasText)
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("未找到SecretKey"));
    }

    protected List<? extends GrantedAuthority> obtainAuthorities(ClientResponse client) {
        return client.getPermissions()
                .stream()
                .map(PermissionResponse::getCode)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("all")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final Optional<String> accessKey = obtainAccessKey(request);
        final Optional<String> timestamp = obtainTimestamp(request);
        final Optional<String> signature = obtainSignature(request);
        if (accessKey.isEmpty() || timestamp.isEmpty() || signature.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }
        final ClientResponse result = obtainClient(accessKey.get());
        final Map<String, String> params = new HashMap<>();
        params.putAll(obtainParams(request));
        ClientAuthenticationToken authenticationToken = new ClientAuthenticationToken(accessKey.get(),
                obtainSecretKey(result), signature.get(), params, obtainAuthorities(result));
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
