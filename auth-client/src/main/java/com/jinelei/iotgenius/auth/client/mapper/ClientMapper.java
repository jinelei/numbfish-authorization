package com.jinelei.iotgenius.auth.client.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jinelei.iotgenius.auth.client.domain.ClientEntity;
import org.apache.ibatis.annotations.Mapper;

@SuppressWarnings("unused")
@Mapper
public interface ClientMapper extends BaseMapper<ClientEntity> {

}
