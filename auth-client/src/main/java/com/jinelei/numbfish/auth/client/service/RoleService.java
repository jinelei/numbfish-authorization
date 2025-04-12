package com.jinelei.numbfish.auth.client.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jinelei.numbfish.auth.client.domain.RoleEntity;
import com.jinelei.numbfish.auth.dto.role.*;
import com.jinelei.numbfish.auth.permission.declaration.RoleDeclaration;

import java.util.List;

@SuppressWarnings("unused")
public interface RoleService extends IService<RoleEntity> {

    void create(RoleCreateRequest request);

    void delete(RoleDeleteRequest request);

    void update(RoleUpdateRequest request);

    RoleEntity get(RoleQueryRequest request);

    List<RoleEntity> tree(RoleQueryRequest request);

    List<RoleEntity> list(RoleQueryRequest request);

    IPage<RoleEntity> page(IPage<RoleEntity> page, RoleQueryRequest request);

    RoleResponse convert(RoleEntity entity);

    List<RoleResponse> convertTree(List<RoleEntity> entity);

    <T extends RoleDeclaration<?>> Boolean regist(List<T> roles);

}
