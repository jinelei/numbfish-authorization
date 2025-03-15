package com.jinelei.iotgenius.auth.client.configuration.permission;

import com.jinelei.iotgenius.auth.enumeration.PermissionType;
import com.jinelei.iotgenius.auth.permission.PermissionDeclaration;

public enum PermissionInstance implements PermissionDeclaration {
    USER_ADMINISTRATOR("user_administrator", "用户管理员", PermissionType.ACTION),
    USER_ROLE_ALLOCATOR("user_role_allocator", "用户角色分配员", PermissionType.ACTION),

    ;

    private final String code;
    private final String description;
    private final PermissionType type;

    private PermissionInstance(String code, String description, PermissionType type) {
        this.code = code;
        this.description = description;
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public PermissionType getType() {
        return type;
    }

}
