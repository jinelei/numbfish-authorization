package com.jinelei.numbfish.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jinelei.numbfish.auth.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@SuppressWarnings("unused")
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {

}
