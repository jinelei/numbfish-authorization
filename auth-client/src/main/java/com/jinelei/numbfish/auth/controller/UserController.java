package com.jinelei.numbfish.auth.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.jinelei.numbfish.auth.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.jinelei.numbfish.auth.api.UserApi;
import com.jinelei.numbfish.auth.entity.UserEntity;
import com.jinelei.numbfish.common.helper.PageHelper;
import com.jinelei.numbfish.auth.service.UserService;
import com.jinelei.numbfish.common.request.PageRequest;
import com.jinelei.numbfish.common.view.BaseView;
import com.jinelei.numbfish.common.view.ListView;
import com.jinelei.numbfish.common.view.PageView;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@SuppressWarnings("unused")
@ApiSupport(order = 3)
@Tag(name = "用户管理")
@Validated
@RestController
@RequestMapping("/user")
public class UserController implements UserApi {

    @Autowired
    protected UserService userService;

    @Override
    @ApiOperationSupport(order = 1)
    @Operation(summary = "创建用户")
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('USER_CREATE')")
    public BaseView<Void> create(@RequestBody @Valid UserCreateRequest request) {
        userService.create(request);
        return new BaseView<>("创建成功");
    }

    @Override
    @ApiOperationSupport(order = 2)
    @Operation(summary = "删除用户")
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    public BaseView<Void> delete(@RequestBody @Valid UserDeleteRequest request) {
        userService.delete(request);
        return new BaseView<>("删除成功");
    }

    @Override
    @ApiOperationSupport(order = 3)
    @Operation(summary = "更新用户")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    public BaseView<Void> update(@RequestBody @Valid UserUpdateRequest request) {
        userService.update(request);
        return new BaseView<>("更新成功");
    }

    @Override
    @ApiOperationSupport(order = 4)
    @Operation(summary = "获取用户")
    @PostMapping("/get")
    @PreAuthorize("hasAuthority('USER_DETAIL')")
    public BaseView<UserResponse> get(@RequestBody @Valid UserQueryRequest request) {
        UserEntity entity = userService.get(request);
        UserResponse convert = userService.convert(entity);
        return new BaseView<>(convert);
    }

    @Override
    @ApiOperationSupport(order = 5)
    @Operation(summary = "获取用户列表")
    @PostMapping("/list")
    @PreAuthorize("hasAuthority('USER_SUMMARY')")
    public ListView<UserResponse> list(@RequestBody @Valid UserQueryRequest request) {
        List<UserEntity> entities = userService.list(request);
        List<UserResponse> convert = entities.parallelStream().map(entity -> userService.convert(entity))
                .collect(Collectors.toList());
        return new ListView<>(convert);
    }

    @Override
    @ApiOperationSupport(order = 6)
    @Operation(summary = "获取用户分页")
    @PostMapping("/page")
    @PreAuthorize("hasAuthority('USER_SUMMARY')")
    public PageView<UserResponse> page(@RequestBody @Valid PageRequest<UserQueryRequest> request) {
        IPage<UserEntity> page = userService.page(PageHelper.toPage(new PageDTO<>(), request),
                Optional.ofNullable(request.getParams()).orElse(new UserQueryRequest()));
        List<UserResponse> collect = page.getRecords().parallelStream().map(entity -> userService.convert(entity))
                .collect(Collectors.toList());
        return new PageView<>(collect, page.getTotal(), page.getPages(), page.getSize());
    }

    @Override
    @ApiOperationSupport(order = 7)
    @Operation(summary = "用户登陆")
    @PostMapping("/login")
    public BaseView<String> login(@RequestBody @Valid UserLoginRequest request) {
        String token = userService.login(request);
        return new BaseView<>("登陆成功", token);
    }

    @Override
    @ApiOperationSupport(order = 8)
    @Operation(summary = "用户登出")
    @PostMapping("/logout")
    @PreAuthorize("hasAuthority('USER_DETAIL')")
    public BaseView<Void> logout() {
        userService.logout();
        return new BaseView<>();
    }

    @Override
    @ApiOperationSupport(order = 9)
    @Operation(summary = "用户修改密码")
    @PostMapping("/updatePassword")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    public BaseView<String> updatePassword(@RequestBody @Valid UserUpdatePasswordRequest request) {
        userService.updatePassword(request);
        return new BaseView<>("修改成功");
    }

    @Override
    @ApiOperationSupport(order = 10)
    @Operation(summary = "用户信息")
    @PostMapping("/info")
    @PreAuthorize("hasAuthority('USER_DETAIL')")
    public BaseView<UserInfoResponse> info() {
        UserInfoResponse info = userService.info();
        return new BaseView<>("查询成功", info);
    }

}
