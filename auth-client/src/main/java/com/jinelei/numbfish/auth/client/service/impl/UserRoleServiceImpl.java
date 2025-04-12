package com.jinelei.numbfish.auth.client.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinelei.numbfish.auth.client.domain.UserRoleEntity;
import com.jinelei.numbfish.auth.client.mapper.UserRoleMapper;
import com.jinelei.numbfish.auth.client.service.UserRoleService;
import org.springframework.stereotype.Service;

@SuppressWarnings("unused")
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRoleEntity>
        implements UserRoleService {
}
