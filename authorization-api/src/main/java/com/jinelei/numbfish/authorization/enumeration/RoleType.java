package com.jinelei.numbfish.authorization.enumeration;

@SuppressWarnings("unused")
public enum RoleType {
    NORMAL(0, "普通用户"),
    ADMIN(1, "管理员");

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
