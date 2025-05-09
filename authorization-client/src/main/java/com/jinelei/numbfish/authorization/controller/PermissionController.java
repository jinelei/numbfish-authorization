package com.jinelei.numbfish.authorization.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.jinelei.numbfish.authorization.api.PermissionApi;
import com.jinelei.numbfish.authorization.entity.PermissionEntity;
import com.jinelei.numbfish.common.helper.PageHelper;
import com.jinelei.numbfish.authorization.service.PermissionService;
import com.jinelei.numbfish.authorization.dto.PermissionCreateRequest;
import com.jinelei.numbfish.authorization.dto.PermissionDeleteRequest;
import com.jinelei.numbfish.authorization.dto.PermissionQueryRequest;
import com.jinelei.numbfish.authorization.dto.PermissionResponse;
import com.jinelei.numbfish.authorization.dto.PermissionUpdateRequest;
import com.jinelei.numbfish.authorization.permission.declaration.PermissionDeclaration;
import com.jinelei.numbfish.common.request.PageRequest;
import com.jinelei.numbfish.common.view.BaseView;
import com.jinelei.numbfish.common.view.ListView;
import com.jinelei.numbfish.common.view.PageView;

import jakarta.validation.Valid;

@SuppressWarnings("unused")
@Validated
@RestController
@RequestMapping("/permission")
public class PermissionController implements PermissionApi {

    @Autowired
    protected PermissionService permissionService;

    @Override
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('PERMISSION_CREATE')")
    public BaseView<Void> create(@RequestBody @Valid PermissionCreateRequest request) {
        permissionService.create(request);
        return new BaseView<>("创建成功");
    }

    @Override
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('PERMISSION_DELETE')")
    public BaseView<Void> delete(@RequestBody @Valid PermissionDeleteRequest request) {
        permissionService.delete(request);
        return new BaseView<>("删除成功");
    }

    @Override
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('PERMISSION_UPDATE')")
    public BaseView<Void> update(@RequestBody @Valid PermissionUpdateRequest request) {
        permissionService.update(request);
        return new BaseView<>("更新成功");
    }

    @Override
    @PostMapping("/get")
    @PreAuthorize("hasAuthority('PERMISSION_DETAIL')")
    public BaseView<PermissionResponse> get(@RequestBody @Valid PermissionQueryRequest request) {
        PermissionEntity entity = permissionService.get(request);
        PermissionResponse convert = permissionService.convert(entity);
        return new BaseView<>(convert);
    }

    @Override
    @PostMapping("/tree")
    @PreAuthorize("hasAuthority('PERMISSION_SUMMARY')")
    public BaseView<List<PermissionResponse>> tree(@RequestBody @Valid PermissionQueryRequest request) {
        List<PermissionEntity> entities = permissionService.tree(request);
        List<PermissionResponse> convert = permissionService.convertTree(entities);
        return new BaseView<>(convert);
    }

    @Override
    @PostMapping("/list")
    @PreAuthorize("hasAuthority('PERMISSION_SUMMARY')")
    public ListView<PermissionResponse> list(@RequestBody @Valid PermissionQueryRequest request) {
        List<PermissionEntity> entities = permissionService.list(request);
        List<PermissionResponse> convert = entities.parallelStream().map(entity -> permissionService.convert(entity))
                .collect(Collectors.toList());
        return new ListView<>(convert);
    }

    @Override
    @PostMapping("/page")
    @PreAuthorize("hasAuthority('PERMISSION_SUMMARY')")
    public PageView<PermissionResponse> page(@RequestBody @Valid PageRequest<PermissionQueryRequest> request) {
        IPage<PermissionEntity> page = permissionService.page(PageHelper.toPage(new PageDTO<>(), request),
                Optional.ofNullable(request.getParams()).orElse(new PermissionQueryRequest()));
        List<PermissionResponse> collect = page.getRecords().parallelStream()
                .map(entity -> permissionService.convert(entity))
                .collect(Collectors.toList());
        return new PageView<>(collect, page.getTotal(), page.getPages(), page.getSize());
    }

    @Override
    @PostMapping("/regist")
    @PreAuthorize("hasAnyAuthority('PERMISSION_CREATE','PERMISSION_UPDATE','PERMISSION_DELETE')")
    public <T extends PermissionDeclaration<?>> BaseView<Boolean> regist(@Valid List<T> permissions) {
        Boolean result = permissionService.regist(Optional.ofNullable(permissions).orElse(new ArrayList<>()));
        return new BaseView<>(result);
    }

}
