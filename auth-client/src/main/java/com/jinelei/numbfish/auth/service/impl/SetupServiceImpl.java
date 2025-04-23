package com.jinelei.numbfish.auth.service.impl;

import com.jinelei.numbfish.auth.configuration.authentication.instance.PermissionInstance;
import com.jinelei.numbfish.auth.service.PermissionService;
import com.jinelei.numbfish.auth.service.RoleService;
import com.jinelei.numbfish.auth.service.SetupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressWarnings("unused")
@Service
public class SetupServiceImpl implements SetupService {
    private static final Logger log = LoggerFactory.getLogger(SetupServiceImpl.class);

    private final RoleService roleService;
    private final PermissionService permissionService;

    public SetupServiceImpl(RoleService roleService, PermissionService permissionService) {
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    public void permissionRegist() {
        permissionService.regist(List.of(PermissionInstance.class.getEnumConstants()));
    }

}