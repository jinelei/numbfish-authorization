package com.jinelei.iotgenius.auth.service;

import com.jinelei.iotgenius.auth.dto.permission.PermissionRequest;
import com.jinelei.iotgenius.auth.dto.permission.PermissionResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PermissionService {
    /**
     * 创建权限
     * @param request 权限请求对象
     * @return 权限响应对象
     */
    PermissionResponse createPermission(PermissionRequest request);

    /**
     * 删除权限
     * @param permissionId 权限id
     */
    void deletePermission(Long permissionId);

    /**
     * 更新权限
     * @param permissionId 权限id
     * @param request 权限请求对象
     * @return 权限响应对象
     */
    PermissionResponse updatePermission(Long permissionId, PermissionRequest request);

    /**
     * 查询权限详情
     * @param permissionId 权限id
     * @return 权限响应对象
     */
    PermissionResponse getPermissionDetail(Long permissionId);

    /**
     * 查询权限列表
     * @return 权限响应对象列表
     */
    List<PermissionResponse> getPermissionList();

    /**
     * 查询权限分页列表
     * @param pageable 分页信息
     * @return 分页的权限响应对象
     */
    Page<PermissionResponse> getPermissionPage(Pageable pageable);
}
