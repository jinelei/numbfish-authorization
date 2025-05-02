package com.jinelei.numbfish.authorization.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinelei.numbfish.authorization.entity.ClientPermissionEntity;
import com.jinelei.numbfish.authorization.mapper.ClientPermissionMapper;
import com.jinelei.numbfish.authorization.service.ClientPermissionService;
import org.springframework.stereotype.Service;

@SuppressWarnings("unused")
@Service
public class ClientPermissionServiceImpl extends ServiceImpl<ClientPermissionMapper, ClientPermissionEntity>
        implements ClientPermissionService {
}
