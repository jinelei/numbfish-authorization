package com.jinelei.iotgenius.auth.property;

import java.util.Objects;

@SuppressWarnings("all")
public class AuthHeaderProperty {
    protected String header;
    protected String placeholder;

    public AuthHeaderProperty() {
    }

    public AuthHeaderProperty(String header, String placeholder) {
        this.header = header;
        this.placeholder = placeholder;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AuthHeaderProperty that = (AuthHeaderProperty) o;
        return Objects.equals(header, that.header) && Objects.equals(placeholder, that.placeholder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(header, placeholder);
    }

    @Override
    public String toString() {
        return "AuthHeaderProperty{" +
                "header='" + header + '\'' +
                ", placeholder='" + placeholder + '\'' +
                '}';
    }
}