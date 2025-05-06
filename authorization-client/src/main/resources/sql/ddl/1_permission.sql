CREATE TABLE permission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '权限实体的唯一标识，自增主键',
    name VARCHAR(255) COMMENT '权限名称',
    code VARCHAR(255) COMMENT '权限代码',
    type INT COMMENT '权限类型',
    sort_value INT DEFAULT 0 COMMENT '权限排序值',
    parent_id BIGINT COMMENT '父权限 ID',
    remark TEXT COMMENT '权限备注信息',
    created_user_id VARCHAR(255) COMMENT '创建人用户 ID',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_user_id VARCHAR(255) COMMENT '更新人用户 ID',
    updated_time TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_name(name),
    UNIQUE KEY uk_code(code),
    FOREIGN KEY (parent_id) REFERENCES permission(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) COMMENT = '权限实体';
