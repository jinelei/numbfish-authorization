package com.jinelei.iotgenius.auth.api.permission;

import com.jinelei.iotgenius.auth.dto.permission.PermissionRequest;
import com.jinelei.iotgenius.auth.dto.permission.PermissionResponse;
import com.jinelei.iotgenius.common.request.PageRequest;
import com.jinelei.iotgenius.common.view.BaseView;
import com.jinelei.iotgenius.common.view.ListView;
import com.jinelei.iotgenius.common.view.PageView;

public interface PermissionApi {
    /**
     * 创建权限
     * 
     * @param request 权限请求对象
     * @return 权限响应对象
     */
    BaseView<PermissionResponse> create(PermissionRequest request);

    /**
     * 删除权限
     * 
     * @param request 权限请求对象
     */
    BaseView<PermissionResponse> delete(PermissionRequest request);

    /**
     * 删除权限
     * 
     * @param id 权限id
     */
    BaseView<PermissionResponse> deleteById(Long id);

    /**
     * 更新权限
     * 
     * @param request 权限请求对象
     * @return 权限响应对象
     */
    BaseView<PermissionResponse> update(PermissionRequest request);

    /**
     * 查询权限详情
     * 
     * @param id 权限id
     * @return 权限响应对象
     */
    BaseView<PermissionResponse> getById(Long id);

    /**
     * 查询权限详情
     * 
     * @param request 权限请求对象
     * @return 权限响应对象
     */
    BaseView<PermissionResponse> get(PermissionRequest request);

    /**
     * 查询权限列表
     * 
     * @param request 权限请求对象
     * @return 权限响应对象列表
     */
    ListView<PermissionResponse> list(PermissionRequest request);

    /**
     * 查询权限分页
     * 
     * @param request 权限请求对象
     * @return 权限响应对象列表
     */
    PageView<PermissionResponse> page(PageRequest<PermissionRequest> request);

}
