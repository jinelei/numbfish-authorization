package com.jinelei.iotgenius.auth.client.configuration.permission.instance;

import com.jinelei.iotgenius.auth.enumeration.RoleType;
import com.jinelei.iotgenius.auth.permission.declaration.RoleDeclaration;

public class RoleInstance {

    public static enum User implements RoleDeclaration {
        ADMINISTRATOR("administrator", "管理员", RoleType.ADMIN),
        ALLOCATOR("allocator", "角色分配员", RoleType.NORMAL),;

        private final String code;
        private final String description;
        private final RoleType type;

        private User(String code, String description, RoleType type) {
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

        public RoleType getType() {
            return type;
        }

    }

}
