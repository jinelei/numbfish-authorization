package com.jinelei.numbfish.auth.api;

import com.jinelei.numbfish.auth.dto.*;
import com.jinelei.numbfish.common.request.PageRequest;
import com.jinelei.numbfish.common.view.BaseView;
import com.jinelei.numbfish.common.view.ListView;
import com.jinelei.numbfish.common.view.PageView;
import jakarta.validation.Valid;

/*
 * @Author: jinelei
 * @Date: 2025-02-28 16:08:10
 * @LastEditTime: 2025-02-28 16:09:00
 * @Description: UserApi
 */
@SuppressWarnings("unused")
public interface UserApi {
    /**
     * 创建用户
     *
     * @param request 用户请求对象
     * @return 用户响应对象
     */
    BaseView<Void> create(@Valid UserCreateRequest request);

    /**
     * 删除用户
     *
     * @param request 用户请求对象
     */
    BaseView<Void> delete(@Valid UserDeleteRequest request);

    /**
     * 更新用户
     *
     * @param request 用户请求对象
     * @return 用户响应对象
     */
    BaseView<Void> update(@Valid UserUpdateRequest request);

    /**
     * 查询用户详情
     *
     * @param request 用户请求对象
     * @return 用户响应对象
     */
    BaseView<UserResponse> get(@Valid UserQueryRequest request);

    /**
     * 查询用户列表
     *
     * @param request 用户请求对象
     * @return 用户响应对象列表
     */
    ListView<UserResponse> list(@Valid UserQueryRequest request);

    /**
     * 查询用户分页
     *
     * @param request 用户请求对象
     * @return 用户响应对象列表
     */
    PageView<UserResponse> page(@Valid PageRequest<UserQueryRequest> request);

    /**
     * 用户登录
     *
     * @param request 用户请求对象
     * @return 用户响应对象
     */
    BaseView<String> login(@Valid UserLoginRequest request);

    /**
     * 用户登出
     *
     * @return 用户响应对象
     */
    BaseView<Void> logout();

    /**
     * 用户修改密码
     *
     * @param request 用户请求对象
     * @return 用户响应对象
     */
    BaseView<String> updatePassword(@Valid UserUpdatePasswordRequest request);


}