package com.jinelei.iotgenius.auth.client.configuration.authentication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.*;
import com.jinelei.iotgenius.auth.property.AuthorizationProperty;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@SuppressWarnings("unused")
public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final Logger log = LoggerFactory.getLogger(TokenAuthenticationFilter.class);
    private final static Function<AuthorizationProperty, RequestMatcher> REQUEST_MATCHER_FUNCTION = property -> {
        List<RequestMatcher> list = new ArrayList<>();
        property.getIgnoreUrls().stream().map(AntPathRequestMatcher::new).forEach(list::add);
        Optional.ofNullable(property.getLoginUrl()).map(AntPathRequestMatcher::new).ifPresent(list::add);
        OrRequestMatcher or = new OrRequestMatcher(list);
        NegatedRequestMatcher not = new NegatedRequestMatcher(or);
        RequestHeaderRequestMatcher header = new RequestHeaderRequestMatcher(property.getTokenHeader());
        return new AndRequestMatcher(not, header);
    };
    private final static Function<String, String> REMOVE_BEARER = token -> {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    };
    private final AuthorizationProperty property;

    public TokenAuthenticationFilter(AuthorizationProperty property, AuthenticationManager authenticationManager) {
        super(REQUEST_MATCHER_FUNCTION.apply(property), authenticationManager);
        this.property = property;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        final String token = Optional.ofNullable(request.getHeader(property.getTokenHeader())).map(REMOVE_BEARER)
                .orElseThrow(() -> new BadCredentialsException("Token is missing"));
        TokenAuthenticationToken authRequest = new TokenAuthenticationToken(token, null);
        return getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }
}
