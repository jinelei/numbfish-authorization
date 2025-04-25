CREATE TABLE client (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '客户端实体的唯一标识，自增主键',
    access_key VARCHAR(255) COMMENT '客户端访问密钥',
    secret_key VARCHAR(255) COMMENT '客户端密钥',
    expired_at TIMESTAMP COMMENT '客户端密钥过期时间',
    remark TEXT COMMENT '角色备注信息',
    created_user_id VARCHAR(255) COMMENT '创建人客户端 ID',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_user_id VARCHAR(255) COMMENT '更新人客户端 ID',
    updated_time TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT(1) DEFAULT 0 COMMENT '是否启用，0 表示启用，1 表示禁用',
    deleted_user_id VARCHAR(255) COMMENT '删除人客户端 ID',
    deleted_time TIMESTAMP COMMENT '删除时间',
    UNIQUE KEY uk_access_key_deleted (access_key, deleted)
) COMMENT = '客户端实体';