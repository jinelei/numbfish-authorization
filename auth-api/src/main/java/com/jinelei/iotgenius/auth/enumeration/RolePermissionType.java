package com.jinelei.iotgenius.auth.enumeration;

@SuppressWarnings("unused")
public enum RolePermissionType {
    WHITE(1, "白名单"),
    BLACK(2, "黑名单");

    private final int value;
    private final String description;

    RolePermissionType(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
