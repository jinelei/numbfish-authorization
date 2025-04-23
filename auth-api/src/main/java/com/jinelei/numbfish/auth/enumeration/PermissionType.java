package com.jinelei.numbfish.auth.enumeration;

@SuppressWarnings("unused")
public enum PermissionType {
    DIRECTORY(0, "目录"),
    MENU(1, "菜单"),
    ACTION(2, "动作");

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
