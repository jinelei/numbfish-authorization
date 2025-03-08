package com.jinelei.iotgenius.auth.api.permission;

import com.jinelei.iotgenius.auth.dto.permission.*;
import com.jinelei.iotgenius.common.request.PageRequest;
import com.jinelei.iotgenius.common.response.TreeResponse;
import com.jinelei.iotgenius.common.view.BaseView;
import com.jinelei.iotgenius.common.view.ListView;
import com.jinelei.iotgenius.common.view.PageView;

import java.util.List;

public interface PermissionApi {
    /**
     * 创建权限
     *
     * @param request 权限请求对象
     * @return 权限响应对象
     */
    BaseView<Void> create(PermissionCreateRequest request);

    /**
     * 删除权限
     *
     * @param request 权限请求对象
     */
    BaseView<Void> delete(PermissionDeleteRequest request);

    /**
     * 更新权限
     *
     * @param request 权限请求对象
     * @return 权限响应对象
     */
    BaseView<Void> update(PermissionUpdateRequest request);

    /**
     * 查询权限详情
     *
     * @param request 权限请求对象
     * @return 权限响应对象
     */
    BaseView<PermissionResponse> get(PermissionQueryRequest request);

    /**
     * 查询权限详情
     *
     * @param request 权限请求对象
     * @return 权限响应对象
     */
    BaseView<List<PermissionResponse>> tree(PermissionQueryRequest request);

    /**
     * 查询权限列表
     *
     * @param request 权限请求对象
     * @return 权限响应对象列表
     */
    ListView<PermissionResponse> list(PermissionQueryRequest request);

    /**
     * 查询权限分页
     *
     * @param request 权限请求对象
     * @return 权限响应对象列表
     */
    PageView<PermissionResponse> page(PageRequest<PermissionQueryRequest> request);

}
