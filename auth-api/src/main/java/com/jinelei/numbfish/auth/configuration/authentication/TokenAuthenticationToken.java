package com.jinelei.numbfish.auth.configuration.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@SuppressWarnings("unused")
public class TokenAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private static final Logger log = LoggerFactory.getLogger(TokenAuthenticationToken.class);
    private final Long userId;
    private final String token;
    private boolean authenticated;

    public TokenAuthenticationToken(Long userId, String token, Collection<? extends GrantedAuthority> authorities) {
        super(authorities,null, authorities);
        this.userId = userId;
        this.token = token;
        this.authenticated = true;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }


    @Override
    public void setAuthenticated(boolean isAuthenticated) {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
        this.authenticated = false;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }
}
