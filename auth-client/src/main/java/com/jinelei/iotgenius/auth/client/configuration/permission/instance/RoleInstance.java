package com.jinelei.iotgenius.auth.client.configuration.permission.instance;

import java.util.List;

import com.jinelei.iotgenius.auth.enumeration.RoleType;
import com.jinelei.iotgenius.auth.permission.declaration.PermissionDeclaration;
import com.jinelei.iotgenius.auth.permission.declaration.RoleDeclaration;

public enum RoleInstance implements RoleDeclaration<RoleInstance> {
    /**
     * 用户相关
     */
    SUPER_ADMINISTRATOR("超级管理员", "super_administrator", null, "超级管理员", RoleType.ADMIN, 10000, List.of()),
    USER_ADMIN("用户管理员", "user_admin", SUPER_ADMINISTRATOR, "用户管理员", RoleType.ADMIN, 10100,
            List.of(PermissionInstance.USER_MANAGE, PermissionInstance.USER_CREATE, PermissionInstance.USER_UPDATE,
                    PermissionInstance.USER_DELETE, PermissionInstance.USER_SUMMARY, PermissionInstance.USER_DETAIL)),
    USER_ALLOCATOR("角色分配员", "user_allocator", USER_ADMIN, "角色分配员", RoleType.NORMAL, 10101,
            List.of(PermissionInstance.USER_MANAGE, PermissionInstance.USER_UPDATE,
                    PermissionInstance.USER_SUMMARY, PermissionInstance.USER_DETAIL)),
                    ;

    private final String name;
    private final String code;
    private final RoleInstance parent;
    private final String remark;
    private final RoleType type;
    private final Integer sortValue;
    private final List<PermissionDeclaration<?>> permissions;

    private RoleInstance(String name, String code, RoleInstance parent, String remark, RoleType type,
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
