package com.jinelei.numbfish.authorization.configuration.authentication.instance;

import java.util.List;

import com.jinelei.numbfish.authorization.enumeration.RoleType;
import com.jinelei.numbfish.authorization.permission.declaration.PermissionDeclaration;
import com.jinelei.numbfish.authorization.permission.declaration.RoleDeclaration;

@SuppressWarnings("unused")
public enum RoleInstance implements RoleDeclaration<RoleInstance> {
    SUPER_ADMINISTRATOR("超级管理员", "super_administrator", null, "超级管理员", RoleType.ADMIN, 10000, List.of()),
    /**
     * 权限相关
     */
    PERMISSION_ADMIN("权限管理员", "permission_admin", SUPER_ADMINISTRATOR, "权限管理员", RoleType.ADMIN, 10100,
            List.of(PermissionInstance.PERMISSION_MANAGE, PermissionInstance.PERMISSION_CREATE,
                    PermissionInstance.PERMISSION_UPDATE,
                    PermissionInstance.PERMISSION_DELETE, PermissionInstance.PERMISSION_SUMMARY,
                    PermissionInstance.PERMISSION_DETAIL)),
    /**
     * 角色相关
     */
    ROLE_ADMIN("角色管理员", "ROLE_ADMIN", SUPER_ADMINISTRATOR, "角色管理员", RoleType.ADMIN, 10200,
            List.of(PermissionInstance.ROLE_MANAGE, PermissionInstance.ROLE_CREATE, PermissionInstance.ROLE_UPDATE,
                    PermissionInstance.ROLE_DELETE, PermissionInstance.ROLE_SUMMARY, PermissionInstance.ROLE_DETAIL)),
    /**
     * 用户相关
     */
    USER_ADMIN("用户管理员", "USER_ADMIN", SUPER_ADMINISTRATOR, "用户管理员", RoleType.ADMIN, 10300,
            List.of(PermissionInstance.USER_MANAGE, PermissionInstance.USER_CREATE, PermissionInstance.USER_UPDATE,
                    PermissionInstance.USER_DELETE, PermissionInstance.USER_SUMMARY, PermissionInstance.USER_DETAIL)),
    USER_ALLOCATOR("角色分配员", "USER_ALLOCATOR", USER_ADMIN, "角色分配员", RoleType.NORMAL, 10301,
            List.of(PermissionInstance.USER_MANAGE, PermissionInstance.USER_UPDATE,
                    PermissionInstance.USER_SUMMARY, PermissionInstance.USER_DETAIL)),
    USER_NORMAL("普通用户", "USER_NORMAL", USER_ADMIN, "普通用户", RoleType.NORMAL, 10302,
            List.of(PermissionInstance.USER_DETAIL, PermissionInstance.ROLE_DETAIL,
                    PermissionInstance.PERMISSION_DETAIL)),
    /**
     * 客户端相关
     */
    CLIENT_ADMIN("客户端管理员", "CLIENT_ADMIN", SUPER_ADMINISTRATOR, "客户端管理员", RoleType.ADMIN, 10400,
            List.of(PermissionInstance.CLIENT_MANAGE, PermissionInstance.CLIENT_CREATE, PermissionInstance.CLIENT_UPDATE,
                    PermissionInstance.CLIENT_DELETE, PermissionInstance.CLIENT_SUMMARY, PermissionInstance.CLIENT_DETAIL)),
    ;

    private final String name;
    private final String code;
    private final RoleInstance parent;
    private final String remark;
    private final RoleType type;
    private final Integer sortValue;
    private final List<PermissionDeclaration<?>> permissions;

    RoleInstance(String name, String code, RoleInstance parent, String remark, RoleType type,
                 Integer sortValue, List<PermissionDeclaration<?>> permissions) {
        this.name = name;
        this.parent = parent;
        this.code = code;
        this.remark = remark;
        this.type = type;
        this.sortValue = sortValue;
        this.permissions = permissions;
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
    public RoleInstance getParent() {
        return parent;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public RoleType getType() {
        return type;
    }

    @Override
    public Integer getSortValue() {
        return sortValue;
    }

    @Override
    public List<PermissionDeclaration<?>> getPermissions() {
        return permissions;
    }

}
