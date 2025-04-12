package com.jinelei.numbfish.auth.client.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinelei.numbfish.auth.client.domain.ClientPermissionEntity;
import com.jinelei.numbfish.auth.client.mapper.ClientPermissionMapper;
import com.jinelei.numbfish.auth.client.service.ClientPermissionService;
import org.springframework.stereotype.Service;

@SuppressWarnings("unused")
@Service
public class ClientPermissionServiceImpl extends ServiceImpl<ClientPermissionMapper, ClientPermissionEntity>
        implements ClientPermissionService {
}
