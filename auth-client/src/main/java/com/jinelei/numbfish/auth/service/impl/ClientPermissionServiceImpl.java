package com.jinelei.numbfish.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinelei.numbfish.auth.entity.ClientPermissionEntity;
import com.jinelei.numbfish.auth.mapper.ClientPermissionMapper;
import com.jinelei.numbfish.auth.service.ClientPermissionService;
import org.springframework.stereotype.Service;

@SuppressWarnings("unused")
@Service
public class ClientPermissionServiceImpl extends ServiceImpl<ClientPermissionMapper, ClientPermissionEntity>
        implements ClientPermissionService {
}
