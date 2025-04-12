package com.jinelei.numbfish.auth.client.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jinelei.numbfish.auth.client.domain.UserRoleEntity;
import org.apache.ibatis.annotations.Mapper;

@SuppressWarnings("unused")
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRoleEntity> {
}
