package com.jinelei.numbfish.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinelei.numbfish.auth.entity.UserRoleEntity;
import com.jinelei.numbfish.auth.mapper.UserRoleMapper;
import com.jinelei.numbfish.auth.service.UserRoleService;
import org.springframework.stereotype.Service;

@SuppressWarnings("unused")
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRoleEntity>
        implements UserRoleService {
}
