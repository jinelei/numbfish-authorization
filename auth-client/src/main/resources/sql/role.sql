DROP TABLE IF EXISTS role;
CREATE TABLE role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '角色实体的唯一标识，自增主键',
    name VARCHAR(255) COMMENT '角色名称',
    code VARCHAR(255) COMMENT '角色代码',
    type VARCHAR(255) COMMENT '角色类型',
    sort_value INT DEFAULT 0 COMMENT '角色排序值',
    parent_id BIGINT COMMENT '父角色 ID',
    remark TEXT COMMENT '角色备注信息',
    created_user_id VARCHAR(255) COMMENT '创建人用户 ID',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_user_id VARCHAR(255) COMMENT '更新人用户 ID',
    updated_time TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT(1) DEFAULT 0 COMMENT '是否启用，0 表示启用，1 表示禁用',
    deleted_user_id VARCHAR(255) COMMENT '删除人用户 ID',
    deleted_time TIMESTAMP COMMENT '删除时间',
    UNIQUE KEY uk_name_deleted (name, deleted),
    UNIQUE KEY uk_code_deleted (code, deleted),
    FOREIGN KEY (parent_id) REFERENCES role(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) COMMENT = '存储角色实体相关信息的表';
