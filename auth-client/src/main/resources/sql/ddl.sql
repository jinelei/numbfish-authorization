DROP TABLE IF EXISTS permission;
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
    deleted TINYINT(1) DEFAULT 0 COMMENT '是否启用，0 表示启用，1 表示禁用',
    deleted_user_id VARCHAR(255) COMMENT '删除人用户 ID',
    deleted_time TIMESTAMP COMMENT '删除时间',
    UNIQUE KEY uk_name_deleted (name, deleted),
    UNIQUE KEY uk_code_deleted (code, deleted),
    FOREIGN KEY (parent_id) REFERENCES permission(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) COMMENT = '存储权限实体相关信息的表';

DROP TABLE IF EXISTS role;
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
    deleted TINYINT(1) DEFAULT 0 COMMENT '是否启用，0 表示启用，1 表示禁用',
    deleted_user_id VARCHAR(255) COMMENT '删除人用户 ID',
    deleted_time TIMESTAMP COMMENT '删除时间',
    UNIQUE KEY uk_name_deleted (name, deleted),
    UNIQUE KEY uk_code_deleted (code, deleted),
    FOREIGN KEY (parent_id) REFERENCES role(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) COMMENT = '存储角色实体相关信息的表';

DROP TABLE IF EXISTS role_permission;
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
) COMMENT = '角色和权限的关联表';

DROP TABLE IF EXISTS user;
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
    deleted TINYINT(1) DEFAULT 0 COMMENT '是否启用，0 表示启用，1 表示禁用',
    deleted_user_id VARCHAR(255) COMMENT '删除人用户 ID',
    deleted_time TIMESTAMP COMMENT '删除时间',
    UNIQUE KEY uk_name_deleted (username, deleted)
) COMMENT = '存储用户实体相关信息的表';

DROP TABLE IF EXISTS user_role;
CREATE TABLE user_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '关联记录的唯一标识，自增主键',
    user_id BIGINT NOT NULL COMMENT '用户 ID，关联 permission 表的 id',
    role_id BIGINT NOT NULL COMMENT '角色 ID，关联 role 表的 id',
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
    FOREIGN KEY (user_id) REFERENCES user(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    UNIQUE KEY uk_user_role_deleted (role_id, user_id, deleted)
) COMMENT = '用户和角色的关联表';

DROP TABLE IF EXISTS client;
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
) COMMENT = '存储客户端实体相关信息的表';

DROP TABLE IF EXISTS client_permission;
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
) COMMENT = '客户端和权限的关联表';