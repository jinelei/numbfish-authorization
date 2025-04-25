CREATE TABLE role_permission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '关联记录的唯一标识，自增主键',
    role_id BIGINT NOT NULL COMMENT '角色 ID，关联 role 表的 id',
    permission_id BIGINT NOT NULL COMMENT '权限 ID，关联 permission 表的 id',
    type INT COMMENT '角色权限关联类型',
    remark TEXT COMMENT '角色备注信息',
    created_user_id VARCHAR(255) COMMENT '创建人用户 ID',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_user_id VARCHAR(255) COMMENT '更新人用户 ID',
    updated_time TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT(1) DEFAULT 0 COMMENT '是否启用，0 表示启用，1 表示禁用',
    deleted_user_id VARCHAR(255) COMMENT '删除人用户 ID',
    deleted_time TIMESTAMP COMMENT '删除时间',
    FOREIGN KEY (role_id) REFERENCES role(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permission(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    UNIQUE KEY uk_role_permission_deleted (role_id, permission_id, deleted)
) COMMENT = '角色和权限关联';

