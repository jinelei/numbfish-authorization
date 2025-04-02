package com.jinelei.iotgenius.auth.client.configuration.permission.instance;

import com.jinelei.iotgenius.auth.enumeration.PermissionType;
import com.jinelei.iotgenius.auth.permission.declaration.PermissionDeclaration;

public enum PermissionInstance implements PermissionDeclaration<PermissionInstance> {
    /**
     * 用户相关
     */
    USER_MANAGE("用户管理", "user_manage", null, "用户管理", PermissionType.DIRECTORY, 100),
    USER_CREATE("创建用户", "user_create", USER_MANAGE, "创建用户", PermissionType.ACTION, 101),
    USER_UPDATE("更新用户", "user_update", USER_MANAGE, "更新用户", PermissionType.ACTION, 102),
    USER_DELETE("删除用户", "user_delete", USER_MANAGE, "删除用户", PermissionType.ACTION, 103),
    USER_SUMMARY("查看用户概要", "user_summary", USER_MANAGE, "查看用户概要", PermissionType.ACTION, 104),
    USER_DETAIL("查看用户详情", "user_detail", USER_MANAGE, "查看用户详情", PermissionType.ACTION, 105),
    ;

    private final String name;
    private final String code;
    private final PermissionInstance parent;
    private final String remark;
    private final PermissionType type;
    private final Integer sortValue;

    private PermissionInstance(String name, String code, PermissionInstance parent, String remark, PermissionType type,
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