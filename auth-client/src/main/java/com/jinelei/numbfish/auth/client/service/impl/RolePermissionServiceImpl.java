package com.jinelei.numbfish.auth.client.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinelei.numbfish.auth.client.domain.RolePermissionEntity;
import com.jinelei.numbfish.auth.client.mapper.RolePermissionMapper;
import com.jinelei.numbfish.auth.client.service.RolePermissionService;
import org.springframework.stereotype.Service;

@SuppressWarnings("unused")
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermissionEntity>
        implements RolePermissionService {
}
