package com.jinelei.iotgenius.auth.property;

@SuppressWarnings("unused")
public class LoginProperty {
    protected String url;

    public LoginProperty() {
    }

    public LoginProperty(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LoginProperty other = (LoginProperty) obj;
        if (url == null) {
            return other.url == null;
        } else {
            return url.equals(other.url);
        }
    }

    @Override
    public String toString() {
        return "Login [url=" + url + "]";
    }

}
