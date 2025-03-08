package com.jinelei.iotgenius.auth.provider.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jinelei.iotgenius.auth.dto.permission.PermissionRequest;
import com.jinelei.iotgenius.auth.provider.domain.PermissionEntity;

public interface PermissionService extends IService<PermissionEntity> {

    Long create(PermissionRequest request);

    Long delete(PermissionRequest request);

    Long update(PermissionRequest request);

}
