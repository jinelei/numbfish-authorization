package com.jinelei.numbfish.authorization.api;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.jinelei.numbfish.authorization.dto.*;
import com.jinelei.numbfish.common.request.PageRequest;
import com.jinelei.numbfish.common.view.BaseView;
import com.jinelei.numbfish.common.view.ListView;
import com.jinelei.numbfish.common.view.PageView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/*
 * @Author: jinelei
 * @Date: 2025-02-28 16:08:10
 * @LastEditTime: 2025-02-28 16:09:00
 * @Description: UserApi
 */
@SuppressWarnings("unused")
@ApiSupport(order = 3)
@Tag(name = "用户管理")
public interface UserApi {
    /**
     * 创建用户
     *
     * @param request 用户请求对象
     * @return 用户响应对象
     */
    @ApiOperationSupport(order = 1)
    @Operation(summary = "创建用户")
    BaseView<Void> create(@Valid UserCreateRequest request);

    /**
     * 删除用户
     *
     * @param request 用户请求对象
     */
    @ApiOperationSupport(order = 2)
    @Operation(summary = "删除用户")
    BaseView<Void> delete(@Valid UserDeleteRequest request);

    /**
     * 更新用户
     *
     * @param request 用户请求对象
     * @return 用户响应对象
     */
    @ApiOperationSupport(order = 3)
    @Operation(summary = "更新用户")
    BaseView<Void> update(@Valid UserUpdateRequest request);

    /**
     * 查询用户详情
     *
     * @param request 用户请求对象
     * @return 用户响应对象
     */
    @ApiOperationSupport(order = 4)
    @Operation(summary = "获取用户")
    BaseView<UserResponse> get(@Valid UserQueryRequest request);

    /**
     * 查询用户列表
     *
     * @param request 用户请求对象
     * @return 用户响应对象列表
     */
    @ApiOperationSupport(order = 5)
    @Operation(summary = "获取用户列表")
    ListView<UserResponse> list(@Valid UserQueryRequest request);

    /**
     * 查询用户分页
     *
     * @param request 用户请求对象
     * @return 用户响应对象列表
     */
    @ApiOperationSupport(order = 6)
    @Operation(summary = "获取用户分页")
    PageView<UserResponse> page(@Valid PageRequest<UserQueryRequest> request);

    /**
     * 用户登录
     *
     * @param request 用户请求对象
     * @return 用户响应对象
     */
    @ApiOperationSupport(order = 7)
    @Operation(summary = "用户登陆")
    BaseView<String> login(@Valid UserLoginRequest request);

    /**
     * 用户登出
     *
     * @return 用户响应对象
     */
    @ApiOperationSupport(order = 8)
    @Operation(summary = "用户登出")
    BaseView<Void> logout();

    /**
     * 用户修改密码
     *
     * @param request 用户请求对象
     * @return 用户响应对象
     */
    @ApiOperationSupport(order = 9)
    @Operation(summary = "用户修改密码")
    BaseView<String> updatePassword(@Valid UserUpdatePasswordRequest request);

    /**
     * 查询用户信息
     *
     * @return 用户响应对象
     */
    @ApiOperationSupport(order = 10)
    @Operation(summary = "用户信息")
    BaseView<UserInfoResponse> info();

}