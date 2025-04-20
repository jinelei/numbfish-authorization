package com.jinelei.numbfish.auth.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jinelei.numbfish.auth.dto.permission.*;
import com.jinelei.numbfish.auth.permission.declaration.PermissionDeclaration;
import com.jinelei.numbfish.auth.entity.PermissionEntity;

import java.util.List;

@SuppressWarnings("unused")
public interface PermissionService extends IService<PermissionEntity> {

    void create(PermissionCreateRequest request);

    void delete(PermissionDeleteRequest request);

    void update(PermissionUpdateRequest request);

    PermissionEntity get(PermissionQueryRequest request);

    List<PermissionEntity> tree(PermissionQueryRequest request);

    List<PermissionEntity> list(PermissionQueryRequest request);

    IPage<PermissionEntity> page(IPage<PermissionEntity> page, PermissionQueryRequest request);

    PermissionResponse convert(PermissionEntity entity);

    List<PermissionResponse> convertTree(List<PermissionEntity> entity);

    <T extends PermissionDeclaration<?>> Boolean regist(List<T> permissions);

}
