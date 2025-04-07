package com.jinelei.iotgenius.auth.permission.declaration;

import java.util.List;

import com.jinelei.iotgenius.auth.enumeration.RoleType;

import io.swagger.v3.oas.annotations.media.Schema;

@SuppressWarnings("unused")
@Schema(description = "角色声明对象")
public interface RoleDeclaration<T> {

    @Schema(description = "角色名称")
    String getName();

    @Schema(description = "角色编码")
    String getCode();

    @Schema(description = "父级角色")
    T getParent();

    @Schema(description = "角色描述")
    String getRemark();

    @Schema(description = "角色类型")
    RoleType getType();

    @Schema(description = "排序值")
    default Integer getSortValue() {
        return 0;
    }

    @Schema(description = "关联权限")
    List<PermissionDeclaration<?>> getPermissions();

}