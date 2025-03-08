package com.jinelei.iotgenius.auth.provider.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jinelei.iotgenius.auth.provider.domain.PermissionEntity;
import org.springframework.stereotype.Repository;

@Mapper
public interface PermissionMapper extends BaseMapper<PermissionEntity> {

}
