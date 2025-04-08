package com.jinelei.iotgenius.auth.authentication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

@SuppressWarnings("unused")
public class ClientAuthenticationToken implements Authentication {
    private static final Logger log = LoggerFactory.getLogger(ClientAuthenticationToken.class);
    private final String accessKey;
    private final String secretKey;
    private final String signature;
    private final Map<String, String> params;
    private boolean authenticated;
    private final Collection<GrantedAuthority> authorities;

    public ClientAuthenticationToken(String accessKey, String secretKey, String signature, Map<String, String> params,
            Collection<? extends GrantedAuthority> authorities) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.signature = signature;
        this.params = params;
        this.authenticated = false;
        this.authorities = new ArrayList<>();
        authorities.forEach(a -> this.authorities.add(a));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void appendAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities.addAll(authorities);
    }

    @Override
    public Object getCredentials() {
        return secretKey;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return accessKey;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return accessKey;
    }

    public static Logger getLog() {
        return log;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getSignature() {
        return signature;
    }

    public Map<String, String> getParams() {
        return params;
    }

}
