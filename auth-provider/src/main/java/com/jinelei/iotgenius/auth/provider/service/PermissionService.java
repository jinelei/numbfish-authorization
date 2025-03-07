package com.jinelei.iotgenius.auth.provider.service;

import com.jinelei.iotgenius.auth.dto.permission.PermissionRequest;

public interface PermissionService {

    Long create(PermissionRequest request);

}
