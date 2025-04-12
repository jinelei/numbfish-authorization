package com.jinelei.numbfish.auth.client.configuration.authentication;

import com.jinelei.numbfish.auth.client.service.UserService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@SuppressWarnings("unused")
public class TokenAuthenticationProvider implements AuthenticationProvider {
    private final StringRedisTemplate redisTemplate;
    private final UserDetailsService userDetailsService;

    public TokenAuthenticationProvider(StringRedisTemplate redisTemplate, UserDetailsService userDetailsService) {
        this.redisTemplate = redisTemplate;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        TokenAuthenticationToken authToken = (TokenAuthenticationToken) authentication;
        String token = (String) authToken.getPrincipal();

        final String key = UserService.GENERATE_TOKEN_INFO.apply(token);
        Optional.ofNullable(key).orElseThrow(() -> new BadCredentialsException("Invalid token key"));
        final String username = redisTemplate.opsForValue().get(key);

        Collection<? extends GrantedAuthority> authorities = Optional.ofNullable(username)
                .map(userDetailsService::loadUserByUsername)
                .map(UserDetails::getAuthorities)
                .orElse(new ArrayList<>());
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return TokenAuthenticationToken.class.isAssignableFrom(authentication);
    }

}