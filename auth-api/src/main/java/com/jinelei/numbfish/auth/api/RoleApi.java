package com.jinelei.numbfish.auth.api;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.jinelei.numbfish.auth.dto.*;
import com.jinelei.numbfish.auth.permission.declaration.RoleDeclaration;
import com.jinelei.numbfish.common.request.PageRequest;
import com.jinelei.numbfish.common.view.BaseView;
import com.jinelei.numbfish.common.view.ListView;
import com.jinelei.numbfish.common.view.PageView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;

@SuppressWarnings("unused")
@ApiSupport(order = 2)
@Tag(name = "角色管理")
public interface RoleApi {
    /**
     * 创建角色
     *
     * @param request 角色请求对象
     * @return 角色响应对象
     */
    @ApiOperationSupport(order = 1)
    @Operation(summary = "创建角色")
    BaseView<Void> create(@Valid RoleCreateRequest request);

    /**
     * 删除角色
     *
     * @param request 角色请求对象
     */
    @ApiOperationSupport(order = 2)
    @Operation(summary = "删除角色")
    BaseView<Void> delete(@Valid RoleDeleteRequest request);

    /**
     * 更新角色
     *
     * @param request 角色请求对象
     * @return 角色响应对象
     */
    @ApiOperationSupport(order = 3)
    @Operation(summary = "更新角色")
    BaseView<Void> update(@Valid RoleUpdateRequest request);

    /**
     * 查询角色详情
     *
     * @param request 角色请求对象
     * @return 角色响应对象
     */
    @ApiOperationSupport(order = 4)
    @Operation(summary = "获取角色")
    BaseView<RoleResponse> get(@Valid RoleQueryRequest request);

    /**
     * 查询角色详情
     *
     * @param request 角色请求对象
     * @return 角色响应对象
     */
    @ApiOperationSupport(order = 5)
    @Operation(summary = "获取角色树")
    BaseView<List<RoleResponse>> tree(@Valid RoleQueryRequest request);

    /**
     * 查询角色列表
     *
     * @param request 角色请求对象
     * @return 角色响应对象列表
     */
    @ApiOperationSupport(order = 6)
    @Operation(summary = "获取角色列表")
    ListView<RoleResponse> list(@Valid RoleQueryRequest request);

    /**
     * 查询角色分页
     *
     * @param request 角色请求对象
     * @return 角色响应对象列表
     */
    @ApiOperationSupport(order = 7)
    @Operation(summary = "获取角色分页")
    PageView<RoleResponse> page(@Valid PageRequest<RoleQueryRequest> request);

    /**
     * 注册角色实例
     *
     * @param <T>   角色实例
     * @param roles 角色列表
     * @return 注册结果
     */
    @ApiOperationSupport(order = 8)
    @Operation(summary = "注册角色")
    <T extends RoleDeclaration<?>> BaseView<Boolean> regist(@Valid List<T> roles);
}
