package com.jinelei.iotgenius.auth.client.configuration.permission.instance;

import com.jinelei.iotgenius.auth.enumeration.RoleType;
import com.jinelei.iotgenius.auth.permission.declaration.RoleDeclaration;

public enum RoleInstance implements RoleDeclaration {
    /**
     * 用户相关
     */
    SUPER_ADMINISTRATOR("super_administrator", "超级管理员", RoleType.ADMIN),
    USER_ADMIN("user_admin", "超级管理员", RoleType.ADMIN),
    USER_ALLOCATOR("user_allocator", "角色分配员", RoleType.NORMAL),
    ;

    private final String code;
    private final String description;
    private final RoleType type;

    private RoleInstance(String code, String description, RoleType type) {
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

    public RoleType getType() {
        return type;
    }

}
