package com.jinelei.iotgenius.auth.client.configuration.authentication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinelei.iotgenius.auth.client.service.UserService;
import com.jinelei.iotgenius.auth.dto.permission.PermissionResponse;
import com.jinelei.iotgenius.auth.dto.user.UserResponse;
import com.jinelei.iotgenius.auth.property.AuthorizationProperty;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final Logger log = LoggerFactory.getLogger(TokenAuthenticationFilter.class);
    private final static Function<AuthorizationProperty, RequestMatcher> REQUEST_MATCHER_FUCTION = property -> {
        List<RequestMatcher> list = new ArrayList<>();
        property.getIgnoreUrls().stream().map(AntPathRequestMatcher::new).forEach(list::add);
        Optional.ofNullable(property.getLoginUrl()).map(AntPathRequestMatcher::new).ifPresent(list::add);
        OrRequestMatcher or = new OrRequestMatcher(list);
        return new NegatedRequestMatcher(or);
    };
    private final AuthorizationProperty property;
    private final ObjectMapper objectMapper;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final RedisTemplate<String, UserResponse> redisTemplate;

    public TokenAuthenticationFilter(AuthorizationProperty property, ObjectMapper objectMapper,
            UserDetailsService userDetailsService, AuthenticationManager authenticationManager,
            RedisTemplate<String, UserResponse> redisTemplate) {
        super(REQUEST_MATCHER_FUCTION.apply(property), authenticationManager);
        this.property = property;
        this.objectMapper = objectMapper;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.redisTemplate = redisTemplate;
    }

    protected Optional<String> obtainToken(HttpServletRequest request) {
        return Optional.ofNullable(property)
                .map(i -> i.getTokenHeader())
                .map(header -> request.getHeader(header))
                .map(s -> s.replace("Bearer ", ""))
                .filter(s -> Objects.nonNull(s) && !s.isEmpty());
    }

    protected List<? extends GrantedAuthority> obtainAuthorities(UserResponse user) {
        return Optional.ofNullable(user)
                .map(UserResponse::getPermissions)
                .stream()
                .flatMap(List::stream)
                .map(PermissionResponse::getCode)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        Optional<Authentication> optional = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication);
        if (optional.isPresent()) {
            return optional.get();
        }
        final Optional<String> token = obtainToken(request);
        if (token.isEmpty()) {
            throw new AuthenticationCredentialsNotFoundException("token不能为空");
        }

        final String keyUserTokenInfo = UserService.GENERATE_TOKEN_INFO.apply(token.get());
        final UserResponse result = redisTemplate.opsForValue().get(keyUserTokenInfo);
        Optional.ofNullable(result).filter(Objects::nonNull)
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("token无效"));
        final String username = Optional.ofNullable(result).map(UserResponse::getUsername)
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("用户名无效"));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                username, "", obtainAuthorities(result));
        // Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        return authenticationToken;
    }
}