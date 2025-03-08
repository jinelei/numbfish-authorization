package com.jinelei.iotgenius.auth.provider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.jinelei.iotgenius.auth.api.permission.PermissionApi;
import com.jinelei.iotgenius.auth.dto.permission.PermissionRequest;
import com.jinelei.iotgenius.auth.dto.permission.PermissionResponse;
import com.jinelei.iotgenius.auth.provider.service.PermissionService;
import com.jinelei.iotgenius.common.request.PageRequest;
import com.jinelei.iotgenius.common.view.BaseView;
import com.jinelei.iotgenius.common.view.ListView;
import com.jinelei.iotgenius.common.view.PageView;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@ApiSupport(order = 1)
@Tag(name = "权限管理")
@RestController
@RequestMapping("/permission")
public class PermissionController implements PermissionApi {

    @Autowired
    protected PermissionService permissionService;

    @Override
    @ApiOperationSupport(order = 1)
    @Operation(summary = "创建权限")
    @PostMapping("/create")
    public BaseView<Long> create(@RequestBody PermissionRequest request) {
        Long created = permissionService.create(request);
        return new BaseView<>("创建成功", created);
    }

    @Override
    @ApiOperationSupport(order = 2)
    @Operation(summary = "删除权限")
    @PostMapping("/delete")
    public BaseView<Long> delete(@RequestBody PermissionRequest request) {
        Long deleted = permissionService.delete(request);
        return new BaseView<>("删除成功", deleted);
    }

    @Override
    @ApiOperationSupport(order = 3)
    @Operation(summary = "删除权限通过ID")
    @PostMapping("/deleteById")
    public BaseView<Long> deleteById(Long id) {
        PermissionRequest request = new PermissionRequest();
        request.setId(id);
        Long deleted = permissionService.delete(request);
        return new BaseView<>("删除成功", deleted);
    }

    @Override
    @ApiOperationSupport(order = 4)
    @Operation(summary = "更新权限")
    @PostMapping("/update")
    public BaseView<Long> update(@RequestBody PermissionRequest request) {
        Long updated = permissionService.update(request);
        return new BaseView<>("更新成功", updated);
    }

    @Override
    @ApiOperationSupport(order = 5)
    @Operation(summary = "获取权限")
    @PostMapping("/get")
    public BaseView<PermissionResponse> get(@RequestBody PermissionRequest request) {
        return new BaseView<>("暂未实现");
    }

    @Override
    @ApiOperationSupport(order = 6)
    @Operation(summary = "获取权限通过ID")
    @PostMapping("/getById")
    public BaseView<PermissionResponse> getById(Long id) {

        return new BaseView<>("暂未实现");
    }

    @Override
    @ApiOperationSupport(order = 7)
    @Operation(summary = "获取权限列表")
    @PostMapping("/list")
    public ListView<PermissionResponse> list(@RequestBody PermissionRequest request) {

        return new ListView<>("暂未实现");
    }

    @Override
    @ApiOperationSupport(order = 8)
    @Operation(summary = "获取权限分页")
    @PostMapping("/page")
    public PageView<PermissionResponse> page(@RequestBody PageRequest<PermissionRequest> request) {

        return new PageView<>("暂未实现");
    }

}
