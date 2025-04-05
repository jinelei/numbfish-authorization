package com.jinelei.iotgenius.auth.property;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@SuppressWarnings("all")
@Component
@ConfigurationProperties(prefix = "iotgenius.authorization")
public class AuthorizationProperty {
    public static final String AUTHORIZATION = "Authorization";
    public static final String USER = "user";
    public static final String S_S = "%s/%s";
    public static final String SLASH_STIRNG = "/";
    public static final String EMPTY_STRING = "";
    public static final String ALL_STRING = "**";
    public static final Function<String, String> REMOVE_SUFFIX = s -> s.endsWith(SLASH_STIRNG) ? s.substring(0, s.length() - 1) : s;
    public static final Function<String, String> REMOVE_PREFFIX = s -> s.startsWith(SLASH_STIRNG) ? s.substring(1) : s;
    protected LoginProperty login;
    protected LogoutProperty logout;
    protected AdminProperty admin;
    protected IgnoreProperty ignore;
    protected HeaderProperty header;
    protected ContextProperty context;

    public String getContextUrl(ServerProperties serverProperties) {
        return S_S.formatted(Optional.ofNullable(serverProperties)
                        .map(ServerProperties::getServlet)
                        .map(ServerProperties.Servlet::getContextPath)
                        .map(REMOVE_SUFFIX)
                        .orElse(EMPTY_STRING),
                Optional.ofNullable(this.context)
                        .map(ContextProperty::getUrl)
                        .map(REMOVE_PREFFIX)
                        .orElse(ALL_STRING)
        );
    }

    public String getLoginUrl(ServerProperties serverProperties) {
        return S_S.formatted(Optional.ofNullable(serverProperties)
                        .map(ServerProperties::getServlet)
                        .map(ServerProperties.Servlet::getContextPath)
                        .map(REMOVE_SUFFIX)
                        .orElse(EMPTY_STRING),
                Optional.ofNullable(this.login)
                        .map(LoginProperty::getUrl)
                        .map(REMOVE_PREFFIX)
                        .orElse(EMPTY_STRING));
    }

    public String getLogoutUrl(ServerProperties serverProperties) {
        return S_S.formatted(Optional.ofNullable(serverProperties)
                        .map(ServerProperties::getServlet)
                        .map(ServerProperties.Servlet::getContextPath)
                        .map(REMOVE_SUFFIX)
                        .orElse(EMPTY_STRING),
                Optional.ofNullable(this.logout)
                        .map(LogoutProperty::getUrl)
                        .map(REMOVE_PREFFIX)
                        .orElse(EMPTY_STRING));
    }

    public List<String> getIgnoreUrls() {
        return Optional.ofNullable(this.ignore)
                .map(IgnoreProperty::getUrls)
                .orElse(new ArrayList<>());
    }

    public String getTokenHeader() {
        return Optional.ofNullable(this.header)
                .map(HeaderProperty::getHeader)
                .orElse(AUTHORIZATION);
    }

    public String getTokenPlaceholder() {
        return Optional.ofNullable(this.header)
                .map(HeaderProperty::getPlaceholder)
                .orElse(USER);
    }

    public LoginProperty getLogin() {
        return login;
    }

    public void setLogin(LoginProperty login) {
        this.login = login;
    }

    public LogoutProperty getLogout() {
        return logout;
    }

    public void setLogout(LogoutProperty logout) {
        this.logout = logout;
    }

    public AdminProperty getAdmin() {
        return admin;
    }

    public void setAdmin(AdminProperty adminProperty) {
        this.admin = adminProperty;
    }

    public IgnoreProperty getIgnore() {
        return ignore;
    }

    public void setIgnore(IgnoreProperty ignore) {
        this.ignore = ignore;
    }

    public HeaderProperty getHeader() {
        return header;
    }

    public void setHeader(HeaderProperty header) {
        this.header = header;
    }

    public ContextProperty getContext() {
        return context;
    }

    public void setContext(ContextProperty context) {
        this.context = context;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AuthorizationProperty that = (AuthorizationProperty) o;
        return Objects.equals(login, that.login) && Objects.equals(logout, that.logout) && Objects.equals(admin, that.admin) && Objects.equals(ignore, that.ignore) && Objects.equals(header, that.header) && Objects.equals(context, that.context);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, logout, admin, ignore, header, context);
    }

    @Override
    public String toString() {
        return "AuthorizationProperty{" +
                "login=" + login +
                ", logout=" + logout +
                ", admin=" + admin +
                ", ignore=" + ignore +
                ", header=" + header +
                ", context=" + context +
                '}';
    }
}
