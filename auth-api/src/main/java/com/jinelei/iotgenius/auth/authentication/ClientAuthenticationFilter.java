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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinelei.iotgenius.auth.dto.client.ClientResponse;
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
    private AuthorizationProperty property;
    private ObjectMapper objectMapper;
    private AuthenticationManager authenticationManager;
    private ClientDetailService clientDetailService;

    public void setProperty(AuthorizationProperty property) {
        this.property = property;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void setClientDetailService(ClientDetailService clientService) {
        this.clientDetailService = clientService;
    }

    protected String obtainSignature(HttpServletRequest request) {
        Optional.ofNullable(property)
                .map(i -> i.getSignatureHeader())
                .map(header -> request.getHeader(header))
                .filter(s -> Objects.nonNull(s) && !s.isEmpty())
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("未找到签名"));
        return "";
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

    protected String obtainAccessKey(HttpServletRequest request) {
        return "";
    }

    protected String obtainSecretKey(HttpServletRequest request) {
        return "";
    }

    @SuppressWarnings("all")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String accessKey = Optional.ofNullable(obtainAccessKey(request))
                .map(String::trim)
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
        // 获取请求中的所有参数
        final Map<String, String> params = obtainParams(request);
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
        if (accessKey != null && secretKey != null) {
            ClientAuthenticationToken authenticationToken = new ClientAuthenticationToken(accessKey, secretKey,
                    signature, params, authorities);
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
