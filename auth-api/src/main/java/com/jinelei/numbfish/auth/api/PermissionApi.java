package com.jinelei.numbfish.auth.api;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.jinelei.numbfish.auth.dto.*;
import com.jinelei.numbfish.auth.permission.declaration.PermissionDeclaration;
import com.jinelei.numbfish.common.request.PageRequest;
import com.jinelei.numbfish.common.view.BaseView;
import com.jinelei.numbfish.common.view.ListView;
import com.jinelei.numbfish.common.view.PageView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;

@SuppressWarnings("unused")
@ApiSupport(order = 1)
@Tag(name = "权限管理")
public interface PermissionApi {
    /**
     * 创建权限
     *
     * @param request 权限请求对象
     * @return 权限响应对象
     */
    @ApiOperationSupport(order = 1)
    @Operation(summary = "创建权限")
    BaseView<Void> create(@Valid PermissionCreateRequest request);

    /**
     * 删除权限
     *
     * @param request 权限请求对象
     */
    @ApiOperationSupport(order = 2)
    @Operation(summary = "删除权限")
    BaseView<Void> delete(@Valid PermissionDeleteRequest request);

    /**
     * 更新权限
     *
     * @param request 权限请求对象
     * @return 权限响应对象
     */
    @ApiOperationSupport(order = 3)
    @Operation(summary = "更新权限")
    BaseView<Void> update(@Valid PermissionUpdateRequest request);

    /**
     * 查询权限详情
     *
     * @param request 权限请求对象
     * @return 权限响应对象
     */
    @ApiOperationSupport(order = 4)
    @Operation(summary = "获取权限")
    BaseView<PermissionResponse> get(@Valid PermissionQueryRequest request);

    /**
     * 查询权限详情
     *
     * @param request 权限请求对象
     * @return 权限响应对象
     */
    @ApiOperationSupport(order = 4)
    @Operation(summary = "获取权限树")
    BaseView<List<PermissionResponse>> tree(@Valid PermissionQueryRequest request);

    /**
     * 查询权限列表
     *
     * @param request 权限请求对象
     * @return 权限响应对象列表
     */
    @ApiOperationSupport(order = 5)
    @Operation(summary = "获取权限列表")
    ListView<PermissionResponse> list(@Valid PermissionQueryRequest request);

    /**
     * 查询权限分页
     *
     * @param request 权限请求对象
     * @return 权限响应对象列表
     */
    @ApiOperationSupport(order = 6)
    @Operation(summary = "获取权限分页")
    PageView<PermissionResponse> page(@Valid PageRequest<PermissionQueryRequest> request);

    /**
     * 注册权限实例
     *
     * @param <T>         权限实例
     * @param permissions 权限列表
     * @return 注册状态
     */
    @ApiOperationSupport(order = 6)
    @Operation(summary = "注册权限")
    <T extends PermissionDeclaration<?>> BaseView<Boolean> regist(@Valid List<T> permissions);
}
