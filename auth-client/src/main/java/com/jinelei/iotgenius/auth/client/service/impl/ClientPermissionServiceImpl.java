package com.jinelei.iotgenius.auth.client.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinelei.iotgenius.auth.client.domain.ClientPermissionEntity;
import com.jinelei.iotgenius.auth.client.mapper.ClientPermissionMapper;
import com.jinelei.iotgenius.auth.client.service.ClientPermissionService;
import org.springframework.stereotype.Service;

@SuppressWarnings("unused")
@Service
public class ClientPermissionServiceImpl extends ServiceImpl<ClientPermissionMapper, ClientPermissionEntity>
        implements ClientPermissionService {
}
