package com.jinelei.iotgenius.auth.client.configuration.filter;

import com.jinelei.iotgenius.auth.client.helper.SpringHelper;
import com.jinelei.iotgenius.auth.client.service.UserService;
import com.jinelei.iotgenius.auth.dto.user.UserResponse;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class LoginUserFilter implements Filter {
    private final static Logger log = LoggerFactory.getLogger(LoginUserFilter.class);

    /**
     * 从请求中获取用户信息
     *
     * @param request  The request to process
     * @param response The response associated with the request
     * @param chain    Provides access to the next filter in the chain for this filter to pass the request and response
     *                 to for further processing
     */
    @SuppressWarnings("unchecked")
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (request instanceof HttpServletRequest httpRequest) {
            Optional.ofNullable(httpRequest.getHeader("Authorization"))
                    .filter(StringUtils::hasLength)
                    .map(s -> s.startsWith("Bearer ") ? s.substring(7) : s)
                    .ifPresentOrElse(token -> {
                        final RedisTemplate<String, UserResponse> redisTemplate = (RedisTemplate<String, UserResponse>) SpringHelper.getBean("userRedisTemplate");
                        final UserResponse user = redisTemplate.opsForValue().get(UserService.GENERATE_TOKEN_INFO.apply(token));
                        Optional.ofNullable(user)
                                .ifPresentOrElse(u -> {
                                    final List<SimpleGrantedAuthority> authorities = new CopyOnWriteArrayList<>();
                                    Optional.ofNullable(u.getRoles())
                                            .ifPresent(roles ->
                                                    roles.parallelStream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.getCode()))
                                                            .forEach(authorities::add));
                                    Optional.ofNullable(u.getPermissions())
                                            .ifPresent(permissions ->
                                                    permissions.parallelStream().map(permission -> new SimpleGrantedAuthority(permission.getCode()))
                                                            .forEach(authorities::add));
                                    RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
                                    attributes.setAttribute("user", u, RequestAttributes.SCOPE_REQUEST);
                                    RequestContextHolder.setRequestAttributes(attributes);
                                    log.info("获取用户信息成功: {} : {}", token, user);
                                    SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
                                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), null, authorities);
                                    SecurityContextHolder.getContext().setAuthentication(authentication);
                                }, () -> {
                                    log.error("获取用户信息失败: user不存在");
                                });
                    }, () -> {
                        log.error("获取用户信息失败: token不合法");
                    });
        }
        chain.doFilter(request, response);
    }
}
