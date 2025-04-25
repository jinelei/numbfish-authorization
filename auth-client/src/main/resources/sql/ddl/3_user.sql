CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '角色实体的唯一标识，自增主键',
    username VARCHAR(255) COMMENT '用户名称',
    password VARCHAR(255) COMMENT '密码',
    avatar VARCHAR(255) COMMENT '用户头像',
    email VARCHAR(255) COMMENT '邮箱',
    phone VARCHAR(255) COMMENT '手机号',
    remark TEXT COMMENT '角色备注信息',
    created_user_id VARCHAR(255) COMMENT '创建人用户 ID',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_user_id VARCHAR(255) COMMENT '更新人用户 ID',
    updated_time TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_name(username)
) COMMENT = '用户实体';
