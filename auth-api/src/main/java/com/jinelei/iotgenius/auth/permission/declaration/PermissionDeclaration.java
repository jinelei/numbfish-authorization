package com.jinelei.iotgenius.auth.permission.declaration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jinelei.iotgenius.auth.enumeration.PermissionType;

import io.swagger.v3.oas.annotations.media.Schema;

@SuppressWarnings("unused")
@Schema(description = "权限声明对象")
public interface PermissionDeclaration<T> {
    Logger log = LoggerFactory.getLogger(PermissionDeclaration.class);

    @Schema(description = "权限名称")
    String getName();

    @Schema(description = "权限编码")
    String getCode();

    @Schema(description = "父级权限")
    T getParent();

    @Schema(description = "权限描述")
    String getRemark();

    @Schema(description = "权限类型")
    PermissionType getType();

    @Schema(description = "排序值")
    default Integer getSortValue() {
        return 0;
    }

}
