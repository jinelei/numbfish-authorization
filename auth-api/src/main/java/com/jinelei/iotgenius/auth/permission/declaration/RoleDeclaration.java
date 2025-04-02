package com.jinelei.iotgenius.auth.permission.declaration;

import java.util.List;

import com.jinelei.iotgenius.auth.enumeration.RoleType;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "角色声明对象")
public interface RoleDeclaration <T> {

    @Schema(description = "角色名称")
    public String getName();

    @Schema(description = "角色编码")
    public String getCode();

    @Schema(description = "父级角色")
    public T getParent();

    @Schema(description = "角色描述")
    public String getRemark();

    @Schema(description = "角色类型")
    public RoleType getType();

    @Schema(description = "排序值")
    public default Integer getSortValue() {
        return 0;
    };

    @Schema(description = "关联权限")
    public List<PermissionDeclaration<?>> getPermissions();

}