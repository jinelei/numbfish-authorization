package com.jinelei.iotgenius.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinelei.iotgenius.auth.dto.user.UserResponse;
import com.jinelei.iotgenius.auth.helper.ServletHelper;
import com.jinelei.iotgenius.auth.property.AuthorizationProperty;
import com.jinelei.iotgenius.common.view.BaseView;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

public class AuthorizeFilter implements Filter {
    private final static Logger log = LoggerFactory.getLogger(AuthorizeFilter.class);
    public static final Function<String, String> GENERATE_TOKEN_INFO = s -> "user:token:info:" + s;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private final RedisTemplate<String, UserResponse> redisTemplate;
    private final AuthorizationProperty property;
    private final ServerProperties serverProperties;
    private final ServletHelper servletHelper;

    public AuthorizeFilter(RedisTemplate<String, UserResponse> redisTemplate, ServerProperties serverProperties, AuthorizationProperty property, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.property = property;
        this.serverProperties = serverProperties;
        this.servletHelper = new ServletHelper(objectMapper);
    }

    /**
     * 从请求中获取用户信息
     *
     * @param request  请求
     * @param response 响应
     * @param chain    链
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (response instanceof HttpServletResponse httpResponse && request instanceof HttpServletRequest httpRequest) {
            final String requestURI = httpRequest.getRequestURI();
            if (!pathMatcher.match(property.getContextUrl(serverProperties), requestURI)) {
                chain.doFilter(request, response);
            } else if (pathMatcher.match(property.getLogoutUrl(serverProperties), requestURI)) {
                getHeaderToken(httpRequest).ifPresentOrElse(token -> {
                    final UserResponse user = redisTemplate.opsForValue().get(GENERATE_TOKEN_INFO.apply(token));
                    Optional.ofNullable(user)
                            .ifPresentOrElse(u -> {
                                redisTemplate.delete(GENERATE_TOKEN_INFO.apply(token));
                                log.info("登出成功: {} : {}", token, user);
                                servletHelper.response(httpRequest, httpResponse, new BaseView<>(200, "登出成功", ""));
                            }, () -> {
                                log.error("登出失败: user不存在");
                                servletHelper.response(httpRequest, httpResponse, new BaseView<>(403, "登出失败", "用户未登录"));
                            });
                }, () -> {
                    log.error("登出失败: token不合法");
                    servletHelper.response(httpRequest, httpResponse, new BaseView<>(500, "登出失败", "token不合法"));
                });
            } else if (pathMatcher.match(property.getLoginUrl(serverProperties), requestURI)) {
                chain.doFilter(request, response);
            } else if (property.getIgnoreUrls().stream().anyMatch(s -> pathMatcher.match(s, requestURI))) {
                chain.doFilter(request, response);
            } else {
                getHeaderToken(httpRequest).ifPresentOrElse(token -> {
                    final UserResponse user = redisTemplate.opsForValue().get(GENERATE_TOKEN_INFO.apply(token));
                    Optional.ofNullable(user)
                            .ifPresentOrElse(u -> {
                                RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
                                attributes.setAttribute(property.getTokenPlaceholder(), u, RequestAttributes.SCOPE_REQUEST);
                                RequestContextHolder.setRequestAttributes(attributes);
                                log.info("登陆成功: {} : {}", token, user);
                                try {
                                    chain.doFilter(request, response);
                                } catch (IOException | ServletException e) {
                                    servletHelper.response(httpRequest, httpResponse, new BaseView<>(403, "登陆失败", "内部错误"));
                                }
                            }, () -> {
                                log.error("登陆失败: user不存在");
                                servletHelper.response(httpRequest, httpResponse, new BaseView<>(403, "登陆失败", "获取用户信息失败"));
                            });
                }, () -> {
                    log.error("登陆失败: token不合法");
                    servletHelper.response(httpRequest, httpResponse, new BaseView<>(403, "登陆失败", "token不合法"));
                });
            }
        }
    }

    private Optional<String> getHeaderToken(HttpServletRequest httpRequest) {
        return Optional.ofNullable(httpRequest.getHeader(property.getTokenHeader()))
                .filter(StringUtils::hasLength)
                .map(s -> s.startsWith("Bearer ") ? s.substring(7) : s);
    }

}
