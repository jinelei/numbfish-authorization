package com.jinelei.iotgenius.auth.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jinelei.iotgenius.auth.dto.permission.*;
import com.jinelei.iotgenius.auth.client.domain.PermissionEntity;

import java.util.List;

public interface PermissionService extends IService<PermissionEntity> {

    void create(PermissionCreateRequest request);

    void delete(PermissionDeleteRequest request);

    void update(PermissionUpdateRequest request);

    PermissionEntity get(PermissionQueryRequest request);

    List<PermissionEntity> tree(PermissionQueryRequest request);

    List<PermissionEntity> list(PermissionQueryRequest request);

    PermissionResponse convert(PermissionEntity entity);

    List<PermissionResponse> convertTree(List<PermissionEntity> entity);

}
