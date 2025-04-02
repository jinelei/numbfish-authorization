package com.jinelei.iotgenius.auth.client.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.jinelei.iotgenius.auth.api.role.RoleApi;
import com.jinelei.iotgenius.auth.client.domain.RoleEntity;
import com.jinelei.iotgenius.auth.client.helper.PageHelper;
import com.jinelei.iotgenius.auth.client.service.RoleService;
import com.jinelei.iotgenius.auth.dto.role.*;
import com.jinelei.iotgenius.auth.permission.declaration.PermissionDeclaration;
import com.jinelei.iotgenius.auth.permission.declaration.RoleDeclaration;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("all")
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
        roleService.create(request);
        return new BaseView<>("创建成功");
    }

    @Override
    @ApiOperationSupport(order = 2)
    @Operation(summary = "删除角色")
    @PostMapping("/delete")
    public BaseView<Void> delete(@RequestBody @Valid RoleDeleteRequest request) {
        roleService.delete(request);
        return new BaseView<>("删除成功");
    }

    @Override
    @ApiOperationSupport(order = 3)
    @Operation(summary = "更新角色")
    @PostMapping("/update")
    public BaseView<Void> update(@RequestBody @Valid RoleUpdateRequest request) {
        roleService.update(request);
        return new BaseView<>("更新成功");
    }

    @Override
    @ApiOperationSupport(order = 4)
    @Operation(summary = "获取角色")
    @PostMapping("/get")
    public BaseView<RoleResponse> get(@RequestBody @Valid RoleQueryRequest request) {
        RoleEntity entity = roleService.get(request);
        RoleResponse convert = roleService.convert(entity);
        return new BaseView<>(convert);
    }

    @Override
    @ApiOperationSupport(order = 4)
    @Operation(summary = "获取角色树")
    @PostMapping("/tree")
    public BaseView<List<RoleResponse>> tree(@RequestBody @Valid RoleQueryRequest request) {
        List<RoleEntity> entities = roleService.tree(request);
        List<RoleResponse> convert = roleService.convertTree(entities);
        return new BaseView<>(convert);
    }

    @Override
    @ApiOperationSupport(order = 5)
    @Operation(summary = "获取角色列表")
    @PostMapping("/list")
    public ListView<RoleResponse> list(@RequestBody @Valid RoleQueryRequest request) {
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
