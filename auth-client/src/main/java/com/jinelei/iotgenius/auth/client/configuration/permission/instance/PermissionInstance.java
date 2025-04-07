package com.jinelei.iotgenius.auth.client.configuration.permission.instance;

import com.jinelei.iotgenius.auth.enumeration.PermissionType;
import com.jinelei.iotgenius.auth.permission.declaration.PermissionDeclaration;

@SuppressWarnings("unused")
public enum PermissionInstance implements PermissionDeclaration<PermissionInstance> {
    /**
     * 客户端相关
     */
    CLIENT_MANAGE("客户端管理", "user_manage", null, "客户端管理", PermissionType.DIRECTORY, 100),
    CLIENT_CREATE("创建客户端", "user_create", CLIENT_MANAGE, "创建客户端", PermissionType.ACTION, 101),
    CLIENT_UPDATE("更新客户端", "user_update", CLIENT_MANAGE, "更新客户端", PermissionType.ACTION, 102),
    CLIENT_DELETE("删除客户端", "user_delete", CLIENT_MANAGE, "删除客户端", PermissionType.ACTION, 103),
    CLIENT_SUMMARY("查看客户端概要", "user_summary", CLIENT_MANAGE, "查看客户端概要", PermissionType.ACTION, 104),
    CLIENT_DETAIL("查看客户端详情", "user_detail", CLIENT_MANAGE, "查看客户端详情", PermissionType.ACTION, 105),
    /**
     * 用户相关
     */
    USER_MANAGE("用户管理", "user_manage", null, "用户管理", PermissionType.DIRECTORY, 100),
    USER_CREATE("创建用户", "user_create", USER_MANAGE, "创建用户", PermissionType.ACTION, 101),
    USER_UPDATE("更新用户", "user_update", USER_MANAGE, "更新用户", PermissionType.ACTION, 102),
    USER_DELETE("删除用户", "user_delete", USER_MANAGE, "删除用户", PermissionType.ACTION, 103),
    USER_SUMMARY("查看用户概要", "user_summary", USER_MANAGE, "查看用户概要", PermissionType.ACTION, 104),
    USER_DETAIL("查看用户详情", "user_detail", USER_MANAGE, "查看用户详情", PermissionType.ACTION, 105),
    /**
     * 角色相关
     */
    ROLE_MANAGE("角色管理", "role_manage", null, "角色管理", PermissionType.DIRECTORY, 200),
    ROLE_CREATE("创建角色", "role_create", ROLE_MANAGE, "创建角色", PermissionType.ACTION, 201),
    ROLE_UPDATE("更新角色", "role_update", ROLE_MANAGE, "更新角色", PermissionType.ACTION, 202),
    ROLE_DELETE("删除角色", "role_delete", ROLE_MANAGE, "删除角色", PermissionType.ACTION, 203),
    ROLE_SUMMARY("查看角色概要", "role_summary", ROLE_MANAGE, "查看角色概要", PermissionType.ACTION, 204),
    ROLE_DETAIL("查看角色详情", "role_detail", ROLE_MANAGE, "查看角色详情", PermissionType.ACTION, 205),
    /**
     * 权限相关
     */
    PERMISSION_MANAGE("权限管理", "permission_manage", null, "权限管理", PermissionType.DIRECTORY, 300),
    PERMISSION_CREATE("创建权限", "permission_create", PERMISSION_MANAGE, "创建权限", PermissionType.ACTION, 301),
    PERMISSION_UPDATE("更新权限", "permission_update", PERMISSION_MANAGE, "更新权限", PermissionType.ACTION, 302),
    PERMISSION_DELETE("删除权限", "permission_delete", PERMISSION_MANAGE, "删除权限", PermissionType.ACTION, 303),
    PERMISSION_SUMMARY("查看权限概要", "permission_summary", PERMISSION_MANAGE, "查看权限概要", PermissionType.ACTION, 304),
    PERMISSION_DETAIL("查看权限详情", "permission_detail", PERMISSION_MANAGE, "查看权限详情", PermissionType.ACTION, 305),
    ;

    private final String name;
    private final String code;
    private final PermissionInstance parent;
    private final String remark;
    private final PermissionType type;
    private final Integer sortValue;

    PermissionInstance(String name, String code, PermissionInstance parent, String remark, PermissionType type,
                       Integer sortValue) {
        this.name = name;
        this.parent = parent;
        this.code = code;
        this.remark = remark;
        this.type = type;
        this.sortValue = sortValue;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public PermissionInstance getParent() {
        return parent;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public PermissionType getType() {
        return type;
    }

    @Override
    public Integer getSortValue() {
        return sortValue;
    }

}