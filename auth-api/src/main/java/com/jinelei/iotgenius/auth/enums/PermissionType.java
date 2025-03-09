package com.jinelei.iotgenius.auth.enums;

import com.fasterxml.jackson.annotation.JsonValue;

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

    // 获取枚举值
    public int getValue() {
        return value;
    }

    // 获取枚举描述
    @JsonValue
    public String getDescription() {
        return description;
    }
}
