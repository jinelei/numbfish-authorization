package com.jinelei.iotgenius.auth.property;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public class AuthIgnoreProperty {
    private List<String> urls = new ArrayList<>();

    public AuthIgnoreProperty() {
    }

    public AuthIgnoreProperty(List<String> urls) {
        this.urls = urls;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AuthIgnoreProperty that = (AuthIgnoreProperty) o;
        return Objects.equals(urls, that.urls);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(urls);
    }

    @Override
    public String toString() {
        return "AuthIgnoreProperty{" +
                "urls=" + urls +
                '}';
    }
}
