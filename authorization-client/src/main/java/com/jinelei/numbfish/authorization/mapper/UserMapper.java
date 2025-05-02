package com.jinelei.numbfish.authorization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jinelei.numbfish.authorization.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@SuppressWarnings("unused")
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {

}
