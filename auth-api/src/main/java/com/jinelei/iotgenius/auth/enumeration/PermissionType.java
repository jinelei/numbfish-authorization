package com.jinelei.iotgenius.auth.enumeration;

@SuppressWarnings("unused")
public enum PermissionType {
    DIRECTORY(1, "目录"),
    MENU(2, "菜单"),
    ACTION(3, "动作");

    private final int value;
    private final String description;

    PermissionType(int value, String description) {
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
