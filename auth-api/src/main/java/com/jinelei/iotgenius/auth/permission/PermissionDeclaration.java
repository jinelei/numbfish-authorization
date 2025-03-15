package com.jinelei.iotgenius.auth.permission;

import com.jinelei.iotgenius.auth.enumeration.PermissionType;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "权限声明对象")
public interface PermissionDeclaration {
    
    @Schema(description = "权限编码")
    public String getCode();

    @Schema(description = "权限描述")
    public String getDescription();

    @Schema(description = "权限类型")
    public PermissionType getType();

}
