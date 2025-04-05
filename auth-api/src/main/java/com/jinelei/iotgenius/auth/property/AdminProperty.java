package com.jinelei.iotgenius.auth.property;

@SuppressWarnings("unused")
public class AdminProperty {
    protected String username;

    public AdminProperty() {
    }

    public AdminProperty(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((username == null) ? 0 : username.hashCode());
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
        AdminProperty other = (AdminProperty) obj;
        if (username == null) {
            return other.username == null;
        } else {
            return username.equals(other.username);
        }
    }

    @Override
    public String toString() {
        return "Admin [username=" + username + "]";
    }

}