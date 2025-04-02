package com.jinelei.iotgenius.auth.client.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.jinelei.iotgenius.auth.client.domain.PermissionEntity;
import com.jinelei.iotgenius.auth.client.helper.PageHelper;
import com.jinelei.iotgenius.auth.dto.permission.*;
import com.jinelei.iotgenius.auth.dto.user.UserResponse;
import com.jinelei.iotgenius.auth.helper.AuthorizationHelper;
import com.jinelei.iotgenius.auth.permission.declaration.PermissionDeclaration;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.jinelei.iotgenius.auth.api.permission.PermissionApi;
import com.jinelei.iotgenius.auth.client.service.PermissionService;
import com.jinelei.iotgenius.common.request.PageRequest;
import com.jinelei.iotgenius.common.view.BaseView;
import com.jinelei.iotgenius.common.view.ListView;
import com.jinelei.iotgenius.common.view.PageView;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@ApiSupport(order = 1)
@Tag(name = "权限管理")
@Validated
@RestController
@RequestMapping("/permission")
public class PermissionController implements PermissionApi {

    @Autowired
    protected PermissionService permissionService;

    @Override
    @ApiOperationSupport(order = 1)
    @Operation(summary = "创建权限")
    @PostMapping("/create")
    public BaseView<Void> create(@RequestBody @Valid PermissionCreateRequest request) {
        permissionService.create(request);
        return new BaseView<>("创建成功");
    }

    @Override
    @ApiOperationSupport(order = 2)
    @Operation(summary = "删除权限")
    @PostMapping("/delete")
    public BaseView<Void> delete(@RequestBody @Valid PermissionDeleteRequest request) {
        permissionService.delete(request);
        return new BaseView<>("删除成功");
    }

    @Override
    @ApiOperationSupport(order = 3)
    @Operation(summary = "更新权限")
    @PostMapping("/update")
    public BaseView<Void> update(@RequestBody @Valid PermissionUpdateRequest request) {
        permissionService.update(request);
        return new BaseView<>("更新成功");
    }

    @Override
    @ApiOperationSupport(order = 4)
    @Operation(summary = "获取权限")
    @PostMapping("/get")
    public BaseView<PermissionResponse> get(@RequestBody @Valid PermissionQueryRequest request) {
        PermissionEntity entity = permissionService.get(request);
        PermissionResponse convert = permissionService.convert(entity);
        return new BaseView<>(convert);
    }

    @Override
    @ApiOperationSupport(order = 4)
    @Operation(summary = "获取权限树")
    @PostMapping("/tree")
    public BaseView<List<PermissionResponse>> tree(@RequestBody @Valid PermissionQueryRequest request) {
        List<PermissionEntity> entities = permissionService.tree(request);
        List<PermissionResponse> convert = permissionService.convertTree(entities);
        return new BaseView<>(convert);
    }

    @Override
    @ApiOperationSupport(order = 5)
    @Operation(summary = "获取权限列表")
    @PostMapping("/list")
    public ListView<PermissionResponse> list(@RequestBody @Valid PermissionQueryRequest request) {
        UserResponse userResponse = AuthorizationHelper.currentUser();
        List<PermissionEntity> entities = permissionService.list(request);
        List<PermissionResponse> convert = entities.parallelStream().map(entity -> permissionService.convert(entity))
                .collect(Collectors.toList());
        return new ListView<>(convert);
    }

    @Override
    @ApiOperationSupport(order = 6)
    @Operation(summary = "获取权限分页")
    @PostMapping("/page")
    public PageView<PermissionResponse> page(@RequestBody @Valid PageRequest<PermissionQueryRequest> request) {
        IPage<PermissionEntity> page = permissionService.page(PageHelper.toPage(new PageDTO<>(), request),
                request.getParams());
        List<PermissionResponse> collect = page.getRecords().parallelStream()
                .map(entity -> permissionService.convert(entity))
                .collect(Collectors.toList());
        return new PageView<>(collect, page.getTotal(), page.getPages(), page.getSize());
    }

    @Override
    public <T extends PermissionDeclaration<?>> BaseView<Boolean> regist(@Valid List<T> permissions) {
        Boolean result = permissionService.regist(Optional.ofNullable(permissions).orElse(new ArrayList<>()));
        return new BaseView<>(result);
    }

}
