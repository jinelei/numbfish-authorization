package com.jinelei.iotgenius.auth.api.permission;

import com.jinelei.iotgenius.auth.dto.permission.PermissionRequest;
import com.jinelei.iotgenius.auth.dto.permission.PermissionResponse;
import java.util.List;

public interface PermissionApi {
    /**
     * 创建权限
     * @param request 权限请求对象
     * @return 权限响应对象
     */
    PermissionResponse create(PermissionRequest request);

    /**
     * 删除权限
     * @param permissionId 权限id
     */
    void delete(Long permissionId);

    /**
     * 更新权限
     * @param permissionId 权限id
     * @param request 权限请求对象
     * @return 权限响应对象
     */
    PermissionResponse update(Long permissionId, PermissionRequest request);

    /**
     * 查询权限详情
     * @param permissionId 权限id
     * @return 权限响应对象
     */
    PermissionResponse get(Long permissionId);

    /**
     * 查询权限列表
     * @return 权限响应对象列表
     */
    List<PermissionResponse> list();

}
