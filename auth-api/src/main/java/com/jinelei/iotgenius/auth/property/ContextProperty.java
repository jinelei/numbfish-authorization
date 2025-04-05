package com.jinelei.iotgenius.auth.property;

import java.util.Objects;

@SuppressWarnings("unused")
public class ContextProperty {
    protected String url;

    public ContextProperty() {
    }

    public ContextProperty(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ContextProperty that = (ContextProperty) o;
        return Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(url);
    }

    @Override
    public String toString() {
        return "ContextProperty{" +
                "url='" + url + '\'' +
                '}';
    }
}