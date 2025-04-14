package com.jinelei.numbfish.auth.property;

import java.util.Objects;

@SuppressWarnings("unused")
public class ClientProperty {
    protected String accessKey;
    protected String secretKey;

    public ClientProperty() {
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ClientProperty that = (ClientProperty) o;
        return Objects.equals(accessKey, that.accessKey) && Objects.equals(secretKey, that.secretKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessKey, secretKey);
    }

    @Override
    public String toString() {
        return "ClientProperty{" +
                "accessKey='" + accessKey + '\'' +
                ", secretKey='" + secretKey + '\'' +
                '}';
    }
}