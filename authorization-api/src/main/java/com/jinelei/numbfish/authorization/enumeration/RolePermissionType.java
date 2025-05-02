package com.jinelei.numbfish.authorization.enumeration;

@SuppressWarnings("unused")
public enum RolePermissionType {
    WHITE(0, "白名单"),
    BLACK(1, "黑名单");

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
