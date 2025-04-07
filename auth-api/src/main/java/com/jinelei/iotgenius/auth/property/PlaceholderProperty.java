package com.jinelei.iotgenius.auth.property;

import java.util.Objects;

@SuppressWarnings("unused")
public class PlaceholderProperty {
    protected String header;
    protected String signature;
    protected String user;
    protected String client;
    protected String authorizeType;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getAuthorizeType() {
        return authorizeType;
    }

    public void setAuthorizeType(String authorizeType) {
        this.authorizeType = authorizeType;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        PlaceholderProperty that = (PlaceholderProperty) object;
        return Objects.equals(header, that.header) && Objects.equals(signature, that.signature) && Objects.equals(user, that.user) && Objects.equals(client, that.client) && Objects.equals(authorizeType, that.authorizeType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(header, signature, user, client, authorizeType);
    }

    @Override
    public String toString() {
        return "PlaceholderProperty{" +
                "header='" + header + '\'' +
                ", signature='" + signature + '\'' +
                ", user='" + user + '\'' +
                ", client='" + client + '\'' +
                ", authorizeType='" + authorizeType + '\'' +
                '}';
    }
}