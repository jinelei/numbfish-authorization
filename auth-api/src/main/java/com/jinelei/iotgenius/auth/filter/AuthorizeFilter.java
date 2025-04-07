package com.jinelei.iotgenius.auth.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinelei.iotgenius.auth.dto.user.UserResponse;
import com.jinelei.iotgenius.auth.enumeration.AuthorizeType;
import com.jinelei.iotgenius.auth.helper.AuthorizationHelper;
import com.jinelei.iotgenius.auth.helper.ServletHelper;
import com.jinelei.iotgenius.auth.property.AuthorizationProperty;
import com.jinelei.iotgenius.common.filter.RepeatHttpServletRequestWrapper;
import com.jinelei.iotgenius.common.view.BaseView;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplicationHook;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class AuthorizeFilter implements Filter {
    private final static Logger log = LoggerFactory.getLogger(AuthorizeFilter.class);
    public static final Function<String, String> GENERATE_USER_TOKEN_INFO = s -> "user:token:info:" + s;
    public static final String BEARER_ = "Bearer ";
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private final RedisTemplate<String, UserResponse> redisTemplate;
    private final ObjectMapper objectMapper;
    private final AuthorizationProperty property;
    private final ServerProperties serverProperties;
    private final ServletHelper servletHelper;

    public AuthorizeFilter(RedisTemplate<String, UserResponse> redisTemplate, ServerProperties serverProperties, AuthorizationProperty property, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
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
            final Supplier<Optional<String>> getHeaderToken = () -> Optional.ofNullable(httpRequest.getHeader(property.getTokenHeader()))
                    .filter(StringUtils::hasLength)
                    .map(s -> s.startsWith(BEARER_) ? s.substring(BEARER_.length()) : s);
            final Supplier<Optional<String>> getHeaderSignature = () -> Optional.ofNullable(httpRequest.getHeader(property.getSignatureHeader()))
                    .filter(StringUtils::hasLength);
            final HttpMethod method = HttpMethod.valueOf(httpRequest.getMethod());
            final MediaType contentType = MediaType.parseMediaType(httpRequest.getContentType());
            final String requestURI = httpRequest.getRequestURI();
            if (!pathMatcher.match(property.getContextUrl(serverProperties), requestURI)) {
                chain.doFilter(request, response);
            } else if (pathMatcher.match(property.getLogoutUrl(serverProperties), requestURI)) {
                getHeaderToken.get().ifPresentOrElse(token -> {
                    final UserResponse user = redisTemplate.opsForValue().get(GENERATE_USER_TOKEN_INFO.apply(token));
                    Optional.ofNullable(user)
                            .ifPresentOrElse(u -> {
                                redisTemplate.delete(GENERATE_USER_TOKEN_INFO.apply(token));
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
                getHeaderToken.get().ifPresentOrElse(token -> {
                    final UserResponse user = redisTemplate.opsForValue().get(GENERATE_USER_TOKEN_INFO.apply(token));
                    Optional.ofNullable(user)
                            .ifPresentOrElse(u -> {
                                final RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
                                attributes.setAttribute(property.getTokenPlaceholderUser(), u, RequestAttributes.SCOPE_REQUEST);
                                attributes.setAttribute(property.getTokenPlaceholderAuthorizeType(), AuthorizeType.USER, RequestAttributes.SCOPE_REQUEST);
                                RequestContextHolder.setRequestAttributes(attributes);
                                log.info("登陆成功: {} : {}", token, user);
                                try {
                                    chain.doFilter(request, response);
                                } catch (IOException | ServletException e) {
                                    servletHelper.response(httpRequest, httpResponse, new BaseView<>(403, "登陆失败", "内部错误"));
                                }
                            }, () -> {
                                // 检查是否是signature登录
                                getHeaderSignature.get().ifPresentOrElse(signature -> {
                                    final Map<String, Object> params = new HashMap<>();
                                    // 当请求方法是POST时，检查Content-Type是否为application/json，检查是否具有body参数
                                    if (HttpMethod.POST.equals(method) && MediaType.APPLICATION_JSON.equals(contentType)) {
                                        if (request instanceof RepeatHttpServletRequestWrapper wrapper) {
                                            try {
                                                Map<String, Object> map = objectMapper.readValue(wrapper.getBody(), new TypeReference<HashMap<String, Object>>() {
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
                                    AuthorizationHelper.checkSignature(params);
                                    final RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
                                    attributes.setAttribute(property.getTokenPlaceholderUser(), property.getAdmin(), RequestAttributes.SCOPE_REQUEST);
                                    attributes.setAttribute(property.getTokenPlaceholderAuthorizeType(), AuthorizeType.CLIENT, RequestAttributes.SCOPE_REQUEST);
                                }, () -> {
                                    log.error("登陆失败: 用户不存在");
                                    servletHelper.response(httpRequest, httpResponse, new BaseView<>(403, "登陆失败", "获取用户信息失败"));
                                });
                            });
                }, () -> {
                    log.error("登陆失败: token不合法");
                    servletHelper.response(httpRequest, httpResponse, new BaseView<>(403, "登陆失败", "token不合法"));
                });
            }
        }
    }

}
