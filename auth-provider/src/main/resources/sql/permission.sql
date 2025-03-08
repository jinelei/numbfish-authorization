CREATE TABLE permission (
    -- 实体的唯一标识，使用 VARCHAR(255) 类型，可根据实际情况修改
    id BIGINT COMMENT '实体的唯一标识',
    -- 权限备注信息
    remark TEXT COMMENT '权限备注',
    -- 是否启用标识，使用 TINYINT(1) 类型表示布尔值
    enabled TINYINT(1) COMMENT '是否启用',
    -- 创建人的用户 ID
    created_user_id BIGINT COMMENT '创建人',
    -- 创建时间，使用 TIMESTAMP 类型存储
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP  COMMENT '创建时间',
    -- 更新人的用户 ID
    updated_user_id BIGINT COMMENT '更新人',
    -- 更新时间，使用 TIMESTAMP 类型存储
    updated_time TIMESTAMP COMMENT '更新时间',
    -- 删除人的用户 ID
    deleted_user_id BIGINT COMMENT '删除人',
    -- 删除时间，使用 TIMESTAMP 类型存储
    deleted_time TIMESTAMP COMMENT '删除时间',
    -- 权限的名称，使用 VARCHAR(255) 类型，不可为空
    name VARCHAR(255) NOT NULL COMMENT '权限的名称',
    -- 权限的代码，使用 VARCHAR(255) 类型，可用于唯一标识权限
    code VARCHAR(255) COMMENT '权限的代码',
    -- 权限的排序值，使用 INT 类型，用于权限的排序
    sort_value INT COMMENT '权限的排序值',
    -- 父权限的 ID，使用 BIGINT 类型，用于构建权限的层级关系
    parent_id BIGINT COMMENT '父权限的 ID',
    -- 表的主键约束
    PRIMARY KEY (id)
) COMMENT = '存储权限实体相关信息的表';
