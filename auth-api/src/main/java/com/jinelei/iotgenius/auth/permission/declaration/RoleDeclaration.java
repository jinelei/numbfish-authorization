package com.jinelei.iotgenius.auth.permission.declaration;

import com.jinelei.iotgenius.auth.enumeration.RoleType;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "角色声明对象")
public interface RoleDeclaration {

    @Schema(description = "角色编码")
    public String getCode();

    @Schema(description = "角色描述")
    public String getDescription();

    @Schema(description = "角色类型")
    public RoleType getType();

}
