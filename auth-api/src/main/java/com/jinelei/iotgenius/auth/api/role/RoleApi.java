package com.jinelei.iotgenius.auth.api.role;

import com.jinelei.iotgenius.auth.dto.role.*;
import com.jinelei.iotgenius.auth.permission.declaration.RoleDeclaration;
import com.jinelei.iotgenius.common.request.PageRequest;
import com.jinelei.iotgenius.common.view.BaseView;
import com.jinelei.iotgenius.common.view.ListView;
import com.jinelei.iotgenius.common.view.PageView;
import jakarta.validation.Valid;

import java.util.List;

@SuppressWarnings("unused")
public interface RoleApi {
    /**
     * 创建角色
     *
     * @param request 角色请求对象
     * @return 角色响应对象
     */
    BaseView<Void> create(@Valid RoleCreateRequest request);

    /**
     * 删除角色
     *
     * @param request 角色请求对象
     */
    BaseView<Void> delete(@Valid RoleDeleteRequest request);

    /**
     * 更新角色
     *
     * @param request 角色请求对象
     * @return 角色响应对象
     */
    BaseView<Void> update(@Valid RoleUpdateRequest request);

    /**
     * 查询角色详情
     *
     * @param request 角色请求对象
     * @return 角色响应对象
     */
    BaseView<RoleResponse> get(@Valid RoleQueryRequest request);

    /**
     * 查询角色详情
     *
     * @param request 角色请求对象
     * @return 角色响应对象
     */
    BaseView<List<RoleResponse>> tree(@Valid RoleQueryRequest request);

    /**
     * 查询角色列表
     *
     * @param request 角色请求对象
     * @return 角色响应对象列表
     */
    ListView<RoleResponse> list(@Valid RoleQueryRequest request);

    /**
     * 查询角色分页
     *
     * @param request 角色请求对象
     * @return 角色响应对象列表
     */
    PageView<RoleResponse> page(@Valid PageRequest<RoleQueryRequest> request);

    /**
     * 注册角色实例
     *
     * @param <T>   角色实例
     * @param roles 角色列表
     * @return 注册结果
     */
    <T extends RoleDeclaration<?>> BaseView<Boolean> regist(@Valid List<T> roles);
}
