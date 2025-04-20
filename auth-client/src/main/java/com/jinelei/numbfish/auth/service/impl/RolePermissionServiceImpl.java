package com.jinelei.numbfish.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinelei.numbfish.auth.entity.RolePermissionEntity;
import com.jinelei.numbfish.auth.mapper.RolePermissionMapper;
import com.jinelei.numbfish.auth.service.RolePermissionService;
import org.springframework.stereotype.Service;

@SuppressWarnings("unused")
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermissionEntity>
        implements RolePermissionService {
}
