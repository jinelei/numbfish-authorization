package com.jinelei.iotgenius.auth.client.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinelei.iotgenius.auth.client.domain.UserRoleEntity;
import com.jinelei.iotgenius.auth.client.mapper.UserRoleMapper;
import com.jinelei.iotgenius.auth.client.service.UserRoleService;
import org.springframework.stereotype.Service;

@SuppressWarnings("unused")
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRoleEntity>
        implements UserRoleService {
}
