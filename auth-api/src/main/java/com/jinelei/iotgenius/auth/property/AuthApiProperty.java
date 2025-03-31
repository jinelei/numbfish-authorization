package com.jinelei.iotgenius.auth.property;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("unused")
@Component
@ConfigurationProperties(prefix = "iotgenius.auth")
public class AuthApiProperty {
    protected AuthLoginProperty login;
    protected AuthLoginProperty logout;
    protected AuthAdminProperty admin;
    protected AuthIgnoreProperty ignore;
    protected AuthHeaderProperty header;

    public String getLoginUrl(ServerProperties serverProperties) {
        return "%s/%s".formatted(Optional.ofNullable(serverProperties)
                .map(ServerProperties::getServlet)
                .map(ServerProperties.Servlet::getContextPath)
                .map(s -> s.endsWith("/") ? s.substring(0, s.length() - 1) : s)
                .orElse(""),
                Optional.ofNullable(this.login)
                        .map(AuthLoginProperty::getUrl)
                        .map(s -> s.startsWith("/") ? s.substring(1) : s)
                        .orElse(""));
    }

    public String getLogoutUrl(ServerProperties serverProperties) {
        return "%s/%s".formatted(Optional.ofNullable(serverProperties)
                .map(ServerProperties::getServlet)
                .map(ServerProperties.Servlet::getContextPath)
                .map(s -> s.endsWith("/") ? s.substring(0, s.length() - 1) : s)
                .orElse(""),
                Optional.ofNullable(this.logout)
                        .map(AuthLoginProperty::getUrl)
                        .map(s -> s.startsWith("/") ? s.substring(1) : s)
                        .orElse(""));
    }

    public List<String> getIgnoreUrls() {
        return Optional.ofNullable(this.ignore)
                .map(AuthIgnoreProperty::getUrls)
                .orElse(new ArrayList<>());
    }

    public String getTokenHeader() {
        return Optional.ofNullable(this.header)
                .map(AuthHeaderProperty::getHeader)
                .orElse("Authorization");
    }

    public String getTokenPlaceholder() {
        return Optional.ofNullable(this.header)
                .map(AuthHeaderProperty::getPlaceholder)
                .orElse("user");
    }

    public AuthLoginProperty getLogin() {
        return login;
    }

    public void setLogin(AuthLoginProperty login) {
        this.login = login;
    }

    public AuthLoginProperty getLogout() {
        return logout;
    }

    public void setLogout(AuthLoginProperty logout) {
        this.logout = logout;
    }

    public AuthAdminProperty getAdmin() {
        return admin;
    }

    public void setAdmin(AuthAdminProperty authAdminProperty) {
        this.admin = authAdminProperty;
    }

    public AuthIgnoreProperty getIgnore() {
        return ignore;
    }

    public void setIgnore(AuthIgnoreProperty ignore) {
        this.ignore = ignore;
    }

    public AuthHeaderProperty getHeader() {
        return header;
    }

    public void setHeader(AuthHeaderProperty header) {
        this.header = header;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass())
            return false;
        AuthApiProperty that = (AuthApiProperty) object;
        return Objects.equals(login, that.login) && Objects.equals(logout, that.logout)
                && Objects.equals(admin, that.admin) && Objects.equals(ignore, that.ignore)
                && Objects.equals(header, that.header);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, logout, admin, ignore, header);
    }

    @Override
    public String toString() {
        return "AuthApiProperty{" +
                "login=" + login +
                ", logout=" + logout +
                ", admin=" + admin +
                ", ignore=" + ignore +
                ", header=" + header +
                '}';
    }
}
