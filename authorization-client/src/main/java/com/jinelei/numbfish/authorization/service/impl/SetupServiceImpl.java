package com.jinelei.numbfish.authorization.service.impl;

import com.jinelei.numbfish.authorization.convertor.UserConvertor;
import com.jinelei.numbfish.authorization.dto.SetupRequest;
import com.jinelei.numbfish.authorization.dto.UserUpdateRequest;
import com.jinelei.numbfish.authorization.entity.UserEntity;
import com.jinelei.numbfish.authorization.service.PermissionService;
import com.jinelei.numbfish.authorization.service.RoleService;
import com.jinelei.numbfish.authorization.service.SetupService;
import com.jinelei.numbfish.authorization.service.UserService;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@SuppressWarnings("unused")
@Service
public class SetupServiceImpl implements SetupService {
    private static final Logger log = LoggerFactory.getLogger(SetupServiceImpl.class);
    public static final String SHOW_TABLES_LIKE = "SHOW TABLES LIKE '%s'";

    private final PermissionService permissionService;
    private final RoleService roleService;
    private final UserService userService;
    private final SqlSessionFactory sqlSessionFactory;
    private final UserConvertor userConvertor;

    public SetupServiceImpl(RoleService roleService, PermissionService permissionService, UserService userService,
            SqlSessionFactory sqlSessionFactory, UserConvertor userConvertorer) {
        this.roleService = roleService;
        this.permissionService = permissionService;
        this.userService = userService;
        this.sqlSessionFactory = sqlSessionFactory;
        this.userConvertor = userConvertorer;
    }

    @Override
    public Boolean checkIsSetup() {
        UserEntity admin = userService.getById(1L);
        return Optional.ofNullable(admin).map(i -> i.getPassword()).map(StringUtils::hasLength).orElse(false);
    }

    @Override
    public Boolean init(SetupRequest request) {
        UserUpdateRequest update = userConvertor.entityFromSetupRequest(request);
        update.setId(1L);
        userService.update(update);
        return true;
    }

}
