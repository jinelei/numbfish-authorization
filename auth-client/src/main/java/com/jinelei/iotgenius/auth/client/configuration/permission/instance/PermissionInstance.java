package com.jinelei.iotgenius.auth.client.configuration.permission.instance;

import com.jinelei.iotgenius.auth.enumeration.PermissionType;
import com.jinelei.iotgenius.auth.permission.declaration.PermissionDeclaration;

public class PermissionInstance {

    public static enum User implements PermissionDeclaration {
        CREATE("create", "创建用户", PermissionType.ACTION),
        UPDATE("update", "更新用户", PermissionType.ACTION),
        DELETE("delete", "删除用户", PermissionType.ACTION),
        SUMMARY("summary", "查看用户概要", PermissionType.ACTION),
        DETAIL("detail", "查看用户详情", PermissionType.ACTION),
        ;

        private final String code;
        private final String description;
        private final PermissionType type;

        private User(String code, String description, PermissionType type) {
            this.code = code;
            this.description = description;
            this.type = type;
        }

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

        public PermissionType getType() {
            return type;
        }

    }

}
