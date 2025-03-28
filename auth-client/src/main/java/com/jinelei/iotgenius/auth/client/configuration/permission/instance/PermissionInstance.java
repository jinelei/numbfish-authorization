package com.jinelei.iotgenius.auth.client.configuration.permission.instance;

import com.jinelei.iotgenius.auth.enumeration.PermissionType;
import com.jinelei.iotgenius.auth.permission.declaration.PermissionDeclaration;

public enum PermissionInstance implements PermissionDeclaration {
    /**
     * 用户相关
     */
    USER_CREATE("User", "user_create", "创建用户", PermissionType.ACTION),
    USER_UPDATE("User", "user_update", "更新用户", PermissionType.ACTION),
    USER_DELETE("User", "user_delete", "删除用户", PermissionType.ACTION),
    USER_SUMMARY("User", "user_summary", "查看用户概要", PermissionType.ACTION),
    USER_DETAIL("User", "user_detail", "查看用户详情", PermissionType.ACTION),
    ;

    private final String group;
    private final String code;
    private final String description;
    private final PermissionType type;

    private PermissionInstance(String group, String code, String description, PermissionType type) {
        this.group = group;
        this.code = code;
        this.description = description;
        this.type = type;
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public PermissionType getType() {
        return type;
    }

}