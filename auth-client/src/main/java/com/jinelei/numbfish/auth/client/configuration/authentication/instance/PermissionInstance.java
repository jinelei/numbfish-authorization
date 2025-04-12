package com.jinelei.numbfish.auth.client.configuration.authentication.instance;

import com.jinelei.numbfish.auth.enumeration.PermissionType;
import com.jinelei.numbfish.auth.permission.declaration.PermissionDeclaration;

@SuppressWarnings("unused")
public enum PermissionInstance implements PermissionDeclaration<PermissionInstance> {
    /**
     * 权限相关
     */
    PERMISSION_MANAGE("权限管理", "PERMISSION_MANAGE", null, "权限管理", PermissionType.DIRECTORY, 100),
    PERMISSION_CREATE("创建权限", "PERMISSION_CREATE", PERMISSION_MANAGE, "创建权限", PermissionType.ACTION, 101),
    PERMISSION_UPDATE("更新权限", "PERMISSION_UPDATE", PERMISSION_MANAGE, "更新权限", PermissionType.ACTION, 102),
    PERMISSION_DELETE("删除权限", "PERMISSION_DELETE", PERMISSION_MANAGE, "删除权限", PermissionType.ACTION, 103),
    PERMISSION_SUMMARY("查看权限概要", "PERMISSION_SUMMARY", PERMISSION_MANAGE, "查看权限概要", PermissionType.ACTION, 104),
    PERMISSION_DETAIL("查看权限详情", "PERMISSION_DETAIL", PERMISSION_MANAGE, "查看权限详情", PermissionType.ACTION, 105),
    /**
     * 角色相关
     */
    ROLE_MANAGE("角色管理", "ROLE_MANAGE", null, "角色管理", PermissionType.DIRECTORY, 200),
    ROLE_CREATE("创建角色", "ROLE_CREATE", ROLE_MANAGE, "创建角色", PermissionType.ACTION, 201),
    ROLE_UPDATE("更新角色", "ROLE_UPDATE", ROLE_MANAGE, "更新角色", PermissionType.ACTION, 202),
    ROLE_DELETE("删除角色", "ROLE_DELETE", ROLE_MANAGE, "删除角色", PermissionType.ACTION, 203),
    ROLE_SUMMARY("查看角色概要", "ROLE_SUMMARY", ROLE_MANAGE, "查看角色概要", PermissionType.ACTION, 204),
    ROLE_DETAIL("查看角色详情", "ROLE_DETAIL", ROLE_MANAGE, "查看角色详情", PermissionType.ACTION, 205),
    /**
     * 用户相关
     */
    USER_MANAGE("用户管理", "USER_MANAGE", null, "用户管理", PermissionType.DIRECTORY, 300),
    USER_CREATE("创建用户", "USER_CREATE", USER_MANAGE, "创建用户", PermissionType.ACTION, 301),
    USER_UPDATE("更新用户", "USER_UPDATE", USER_MANAGE, "更新用户", PermissionType.ACTION, 302),
    USER_DELETE("删除用户", "USER_DELETE", USER_MANAGE, "删除用户", PermissionType.ACTION, 303),
    USER_SUMMARY("查看用户概要", "USER_SUMMARY", USER_MANAGE, "查看用户概要", PermissionType.ACTION, 304),
    USER_DETAIL("查看用户详情", "USER_DETAIL", USER_MANAGE, "查看用户详情", PermissionType.ACTION, 305),
    /**
     * 客户端相关
     */
    CLIENT_MANAGE("客户端管理", "CLIENT_MANAGE", null, "客户端管理", PermissionType.DIRECTORY, 400),
    CLIENT_CREATE("创建客户端", "CLIENT_CREATE", CLIENT_MANAGE, "创建客户端", PermissionType.ACTION, 401),
    CLIENT_UPDATE("更新客户端", "CLIENT_UPDATE", CLIENT_MANAGE, "更新客户端", PermissionType.ACTION, 402),
    CLIENT_DELETE("删除客户端", "CLIENT_DELETE", CLIENT_MANAGE, "删除客户端", PermissionType.ACTION, 403),
    CLIENT_SUMMARY("查看客户端概要", "CLIENT_SUMMARY", CLIENT_MANAGE, "查看客户端概要", PermissionType.ACTION, 404),
    CLIENT_DETAIL("查看客户端详情", "CLIENT_DETAIL", CLIENT_MANAGE, "查看客户端详情", PermissionType.ACTION, 405),
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