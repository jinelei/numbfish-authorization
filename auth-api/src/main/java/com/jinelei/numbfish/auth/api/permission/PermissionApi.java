package com.jinelei.numbfish.auth.api.permission;

import com.jinelei.numbfish.auth.dto.permission.*;
import com.jinelei.numbfish.auth.permission.declaration.PermissionDeclaration;
import com.jinelei.numbfish.common.request.PageRequest;
import com.jinelei.numbfish.common.view.BaseView;
import com.jinelei.numbfish.common.view.ListView;
import com.jinelei.numbfish.common.view.PageView;
import jakarta.validation.Valid;

import java.util.List;

@SuppressWarnings("unused")
public interface PermissionApi {
    /**
     * 创建权限
     *
     * @param request 权限请求对象
     * @return 权限响应对象
     */
    BaseView<Void> create(@Valid PermissionCreateRequest request);

    /**
     * 删除权限
     *
     * @param request 权限请求对象
     */
    BaseView<Void> delete(@Valid PermissionDeleteRequest request);

    /**
     * 更新权限
     *
     * @param request 权限请求对象
     * @return 权限响应对象
     */
    BaseView<Void> update(@Valid PermissionUpdateRequest request);

    /**
     * 查询权限详情
     *
     * @param request 权限请求对象
     * @return 权限响应对象
     */
    BaseView<PermissionResponse> get(@Valid PermissionQueryRequest request);

    /**
     * 查询权限详情
     *
     * @param request 权限请求对象
     * @return 权限响应对象
     */
    BaseView<List<PermissionResponse>> tree(@Valid PermissionQueryRequest request);

    /**
     * 查询权限列表
     *
     * @param request 权限请求对象
     * @return 权限响应对象列表
     */
    ListView<PermissionResponse> list(@Valid PermissionQueryRequest request);

    /**
     * 查询权限分页
     *
     * @param request 权限请求对象
     * @return 权限响应对象列表
     */
    PageView<PermissionResponse> page(@Valid PageRequest<PermissionQueryRequest> request);

    /**
     * 注册权限实例
     *
     * @param <T>         权限实例
     * @param permissions 权限列表
     * @return 注册状态
     */
    <T extends PermissionDeclaration<?>> BaseView<Boolean> regist(@Valid List<T> permissions);
}
