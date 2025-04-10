package com.jinelei.iotgenius.auth.client.configuration.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@SuppressWarnings("unused")
public class TokenAuthenticationToken extends AbstractAuthenticationToken {
    private static final Logger log = LoggerFactory.getLogger(TokenAuthenticationToken.class);
    private final String token;
    private boolean authenticated;
    private final Collection<GrantedAuthority> authorities;

    public TokenAuthenticationToken(String token, Collection<GrantedAuthority> authorities) {
        super(authorities);
        this.token = token;
        this.authorities = authorities;
        this.authenticated = true;
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
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
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
        this.authorities.clear();
    }
}
