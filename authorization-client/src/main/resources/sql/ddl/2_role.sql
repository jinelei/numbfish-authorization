CREATE TABLE role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '角色实体的唯一标识，自增主键',
    name VARCHAR(255) COMMENT '角色名称',
    code VARCHAR(255) COMMENT '角色代码',
    type INT COMMENT '角色类型',
    sort_value INT DEFAULT 0 COMMENT '角色排序值',
    parent_id BIGINT COMMENT '父角色 ID',
    remark TEXT COMMENT '角色备注信息',
    created_user_id VARCHAR(255) COMMENT '创建人用户 ID',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_user_id VARCHAR(255) COMMENT '更新人用户 ID',
    updated_time TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_name(name),
    UNIQUE KEY uk_code(code),
    FOREIGN KEY (parent_id) REFERENCES role(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) COMMENT = '角色实体';