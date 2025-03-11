package com.jinelei.iotgenius.auth.client.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.jinelei.iotgenius.auth.api.user.UserApi;
import com.jinelei.iotgenius.auth.client.domain.UserEntity;
import com.jinelei.iotgenius.auth.client.helper.PageHelper;
import com.jinelei.iotgenius.auth.client.service.UserService;
import com.jinelei.iotgenius.auth.dto.user.*;
import com.jinelei.iotgenius.common.request.PageRequest;
import com.jinelei.iotgenius.common.view.BaseView;
import com.jinelei.iotgenius.common.view.ListView;
import com.jinelei.iotgenius.common.view.PageView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

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
    public BaseView<Void> create(@RequestBody @Valid UserCreateRequest request) {
        userService.create(request);
        return new BaseView<>("创建成功");
    }

    @Override
    @ApiOperationSupport(order = 2)
    @Operation(summary = "删除用户")
    @PostMapping("/delete")
    public BaseView<Void> delete(@RequestBody @Valid UserDeleteRequest request) {
        userService.delete(request);
        return new BaseView<>("删除成功");
    }

    @Override
    @ApiOperationSupport(order = 3)
    @Operation(summary = "更新用户")
    @PostMapping("/update")
    public BaseView<Void> update(@RequestBody @Valid UserUpdateRequest request) {
        userService.update(request);
        return new BaseView<>("更新成功");
    }

    @Override
    @ApiOperationSupport(order = 4)
    @Operation(summary = "获取用户")
    @PostMapping("/get")
    public BaseView<UserResponse> get(@RequestBody @Valid UserQueryRequest request) {
        UserEntity entity = userService.get(request);
        UserResponse convert = userService.convert(entity);
        return new BaseView<>(convert);
    }

    @Override
    @ApiOperationSupport(order = 5)
    @Operation(summary = "获取用户列表")
    @PostMapping("/list")
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
    public PageView<UserResponse> page(@RequestBody @Valid PageRequest<UserQueryRequest> request) {
        IPage<UserEntity> page = userService.page(PageHelper.toPage(new PageDTO<>(), request), request.getParams());
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
        return new BaseView<>(token);
    }

    @Override
    @ApiOperationSupport(order = 8)
    @Operation(summary = "用户登出")
    @PostMapping("/logout")
    public BaseView<Void> logout() {
        userService.logout();
        return new BaseView<>();
    }

}
