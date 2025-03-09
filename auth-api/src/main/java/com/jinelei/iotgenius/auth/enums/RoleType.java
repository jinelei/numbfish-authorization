package com.jinelei.iotgenius.auth.enums;

public enum RoleType {
    NORMAL(1, "普通用户"),
    ADMIN(2, "管理员");

    private final int value;
    private final String description;

    RoleType(int value, String description) {
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
