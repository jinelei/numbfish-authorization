CREATE TABLE client_permission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '关联记录的唯一标识，自增主键',
    client_id BIGINT NOT NULL COMMENT '客户端 ID，关联 client 表的 id',
    permission_id BIGINT NOT NULL COMMENT '权限 ID，关联 permission 表的 id',
    type INT COMMENT '客户端权限关联类型',
    remark TEXT COMMENT '客户端备注信息',
    created_user_id VARCHAR(255) COMMENT '创建人用户 ID',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_user_id VARCHAR(255) COMMENT '更新人用户 ID',
    updated_time TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT(1) DEFAULT 0 COMMENT '是否启用，0 表示启用，1 表示禁用',
    deleted_user_id VARCHAR(255) COMMENT '删除人用户 ID',
    deleted_time TIMESTAMP COMMENT '删除时间',
    FOREIGN KEY (client_id) REFERENCES client(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permission(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    UNIQUE KEY uk_client_permission_deleted (client_id, permission_id, deleted)
) COMMENT = '客户端和权限关联';