package com.jinelei.numbfish.authorization.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinelei.numbfish.authorization.entity.UserRoleEntity;
import com.jinelei.numbfish.authorization.mapper.UserRoleMapper;
import com.jinelei.numbfish.authorization.service.UserRoleService;
import org.springframework.stereotype.Service;

@SuppressWarnings("unused")
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRoleEntity>
        implements UserRoleService {
}
