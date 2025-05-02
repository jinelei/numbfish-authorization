package com.jinelei.numbfish.authorization.configuration.authentication;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
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
    private final TokenCacheKeyGenerator cacheKeyGenerator;

    public TokenAuthenticationProvider(StringRedisTemplate redisTemplate, UserDetailsService userDetailsService, TokenCacheKeyGenerator cacheKeyGenerator) {
        this.redisTemplate = redisTemplate;
        this.userDetailsService = userDetailsService;
        this.cacheKeyGenerator = cacheKeyGenerator;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        TokenAuthenticationToken authToken = (TokenAuthenticationToken) authentication;
        String token = (String) authToken.getPrincipal();
        final String keyUserTokenInfo = cacheKeyGenerator.apply(token);
        Optional.ofNullable(keyUserTokenInfo).orElseThrow(() -> new BadCredentialsException("Invalid token keyUserTokenInfo"));
        final String username = Optional.ofNullable(redisTemplate.opsForHash().get(keyUserTokenInfo, "username"))
                .map(Object::toString).orElseThrow(() -> new BadCredentialsException("Invalid token username"));
        final Long id = Optional.ofNullable(redisTemplate.opsForHash().get(keyUserTokenInfo, "id"))
                .map(Object::toString).map(Long::parseLong).orElseThrow(() -> new BadCredentialsException("Invalid token id"));
        Collection<? extends GrantedAuthority> authorities = Optional.ofNullable(username)
                .map(userDetailsService::loadUserByUsername)
                .map(UserDetails::getAuthorities)
                .orElse(new ArrayList<>());
        return new TokenAuthenticationToken(id, username, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return TokenAuthenticationToken.class.isAssignableFrom(authentication);
    }

}