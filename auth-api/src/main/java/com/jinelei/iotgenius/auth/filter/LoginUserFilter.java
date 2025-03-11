package com.jinelei.iotgenius.auth.filter;

import com.jinelei.iotgenius.auth.dto.user.UserResponse;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

public class LoginUserFilter implements Filter {
    private final static Logger log = LoggerFactory.getLogger(LoginUserFilter.class);
    public static final Function<String, String> GENERATE_TOKEN_INFO = s -> "user:token:info:" + s;

    private final RedisTemplate<String, UserResponse> redisTemplate;

    public LoginUserFilter(RedisTemplate<String, UserResponse> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 从请求中获取用户信息
     *
     * @param request  请求
     * @param response 响应
     * @param chain    链
     */
    @SuppressWarnings("unchecked")
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (request instanceof HttpServletRequest httpRequest) {
            Optional.ofNullable(httpRequest.getHeader("Authorization"))
                    .filter(StringUtils::hasLength)
                    .map(s -> s.startsWith("Bearer ") ? s.substring(7) : s)
                    .ifPresentOrElse(token -> {
                        final UserResponse user = redisTemplate.opsForValue().get(GENERATE_TOKEN_INFO.apply(token));
                        Optional.ofNullable(user)
                                .ifPresentOrElse(u -> {
                                    RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
                                    attributes.setAttribute("user", u, RequestAttributes.SCOPE_REQUEST);
                                    RequestContextHolder.setRequestAttributes(attributes);
                                    log.info("获取用户信息成功: {} : {}", token, user);
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
