package com.jinelei.numbfish.auth.client.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.jinelei.numbfish.auth.api.role.RoleApi;
import com.jinelei.numbfish.auth.client.configuration.authentication.permission.instance.PermissionInstance;
import com.jinelei.numbfish.auth.client.domain.RoleEntity;
import com.jinelei.numbfish.auth.client.helper.PageHelper;
import com.jinelei.numbfish.auth.client.service.RoleService;
import com.jinelei.numbfish.auth.dto.role.RoleCreateRequest;
import com.jinelei.numbfish.auth.dto.role.RoleDeleteRequest;
import com.jinelei.numbfish.auth.dto.role.RoleQueryRequest;
import com.jinelei.numbfish.auth.dto.role.RoleResponse;
import com.jinelei.numbfish.auth.dto.role.RoleUpdateRequest;
import com.jinelei.numbfish.auth.helper.AuthorizationHelper;
import com.jinelei.numbfish.auth.permission.declaration.RoleDeclaration;
import com.jinelei.numbfish.common.request.PageRequest;
import com.jinelei.numbfish.common.view.BaseView;
import com.jinelei.numbfish.common.view.ListView;
import com.jinelei.numbfish.common.view.PageView;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@SuppressWarnings("unused")
@ApiSupport(order = 2)
@Tag(name = "角色管理")
@Validated
@RestController
@RequestMapping("/role")
public class RoleController implements RoleApi {

    @Autowired
    protected RoleService roleService;

    @Override
    @ApiOperationSupport(order = 1)
    @Operation(summary = "创建角色")
    @PostMapping("/create")
    public BaseView<Void> create(@RequestBody @Valid RoleCreateRequest request) {
        AuthorizationHelper.hasPermissions(PermissionInstance.ROLE_CREATE);
        roleService.create(request);
        return new BaseView<>("创建成功");
    }

    @Override
    @ApiOperationSupport(order = 2)
    @Operation(summary = "删除角色")
    @PostMapping("/delete")
    public BaseView<Void> delete(@RequestBody @Valid RoleDeleteRequest request) {
        AuthorizationHelper.hasPermissions(PermissionInstance.ROLE_DELETE);
        roleService.delete(request);
        return new BaseView<>("删除成功");
    }

    @Override
    @ApiOperationSupport(order = 3)
    @Operation(summary = "更新角色")
    @PostMapping("/update")
    public BaseView<Void> update(@RequestBody @Valid RoleUpdateRequest request) {
        AuthorizationHelper.hasPermissions(PermissionInstance.ROLE_UPDATE);
        roleService.update(request);
        return new BaseView<>("更新成功");
    }

    @Override
    @ApiOperationSupport(order = 4)
    @Operation(summary = "获取角色")
    @PostMapping("/get")
    public BaseView<RoleResponse> get(@RequestBody @Valid RoleQueryRequest request) {
        AuthorizationHelper.hasPermissions(PermissionInstance.ROLE_DETAIL);
        RoleEntity entity = roleService.get(request);
        RoleResponse convert = roleService.convert(entity);
        return new BaseView<>(convert);
    }

    @Override
    @ApiOperationSupport(order = 4)
    @Operation(summary = "获取角色树")
    @PostMapping("/tree")
    public BaseView<List<RoleResponse>> tree(@RequestBody @Valid RoleQueryRequest request) {
        AuthorizationHelper.hasPermissions(PermissionInstance.ROLE_SUMMARY);
        List<RoleEntity> entities = roleService.tree(request);
        List<RoleResponse> convert = roleService.convertTree(entities);
        return new BaseView<>(convert);
    }

    @Override
    @ApiOperationSupport(order = 5)
    @Operation(summary = "获取角色列表")
    @PostMapping("/list")
    public ListView<RoleResponse> list(@RequestBody @Valid RoleQueryRequest request) {
        AuthorizationHelper.hasPermissions(PermissionInstance.ROLE_SUMMARY);
        List<RoleEntity> entities = roleService.list(request);
        List<RoleResponse> convert = entities.parallelStream().map(entity -> roleService.convert(entity))
                .collect(Collectors.toList());
        return new ListView<>(convert);
    }

    @Override
    @ApiOperationSupport(order = 6)
    @Operation(summary = "获取角色分页")
    @PostMapping("/page")
    public PageView<RoleResponse> page(@RequestBody @Valid PageRequest<RoleQueryRequest> request) {
        AuthorizationHelper.hasPermissions(PermissionInstance.ROLE_SUMMARY);
        IPage<RoleEntity> page = roleService.page(PageHelper.toPage(new PageDTO<>(), request), request.getParams());
        List<RoleResponse> collect = page.getRecords().parallelStream().map(entity -> roleService.convert(entity))
                .collect(Collectors.toList());
        return new PageView<>(collect, page.getTotal(), page.getPages(), page.getSize());
    }

    @Override
    public <T extends RoleDeclaration<?>> BaseView<Boolean> regist(@Valid List<T> roles) {
        Boolean result = roleService.regist(Optional.ofNullable(roles).orElse(new ArrayList<>()));
        return new BaseView<>(result);
    }
}
