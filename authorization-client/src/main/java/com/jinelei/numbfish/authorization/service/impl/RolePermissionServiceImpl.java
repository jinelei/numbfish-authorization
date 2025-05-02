package com.jinelei.numbfish.authorization.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinelei.numbfish.authorization.entity.RolePermissionEntity;
import com.jinelei.numbfish.authorization.mapper.RolePermissionMapper;
import com.jinelei.numbfish.authorization.service.RolePermissionService;
import org.springframework.stereotype.Service;

@SuppressWarnings("unused")
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermissionEntity>
        implements RolePermissionService {
}
