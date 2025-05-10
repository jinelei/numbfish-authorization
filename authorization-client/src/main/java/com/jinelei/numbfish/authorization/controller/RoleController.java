package com.jinelei.numbfish.authorization.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.jinelei.numbfish.authorization.convertor.RoleConvertor;
import com.jinelei.numbfish.authorization.dto.*;
import com.jinelei.numbfish.authorization.enumeration.TreeBuildMode;
import com.jinelei.numbfish.authorization.mapper.RoleMapper;
import com.jinelei.numbfish.common.entity.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.jinelei.numbfish.authorization.api.RoleApi;
import com.jinelei.numbfish.authorization.entity.RoleEntity;
import com.jinelei.numbfish.common.helper.PageHelper;
import com.jinelei.numbfish.authorization.service.RoleService;
import com.jinelei.numbfish.common.request.PageRequest;
import com.jinelei.numbfish.common.view.BaseView;
import com.jinelei.numbfish.common.view.ListView;
import com.jinelei.numbfish.common.view.PageView;

import jakarta.validation.Valid;

@SuppressWarnings("unused")
@Validated
@RestController
@RequestMapping("/role")
public class RoleController implements RoleApi {

    @Autowired
    protected RoleService roleService;
    @Autowired
    private RoleConvertor roleConvertor;

    @Override
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_CREATE')")
    public BaseView<Void> create(@RequestBody @Valid RoleCreateRequest request) {
        roleService.create(request);
        return new BaseView<>("创建成功");
    }

    @Override
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('ROLE_DELETE')")
    public BaseView<Void> delete(@RequestBody @Valid RoleDeleteRequest request) {
        roleService.delete(request);
        return new BaseView<>("删除成功");
    }

    @Override
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('ROLE_UPDATE')")
    public BaseView<Void> update(@RequestBody @Valid RoleUpdateRequest request) {
        roleService.update(request);
        return new BaseView<>("更新成功");
    }

    @Override
    @PostMapping("/get")
    @PreAuthorize("hasAuthority('ROLE_DETAIL')")
    public BaseView<RoleResponse> get(@RequestBody @Valid RoleQueryRequest request) {
        RoleEntity entity = roleService.get(request);
        RoleResponse response = roleConvertor.entityToResponse(entity);
        return new BaseView<>(response);
    }

    @Override
    @PostMapping("/list")
    @PreAuthorize("hasAuthority('ROLE_SUMMARY')")
    public ListView<RoleResponse> list(@RequestBody @Valid RoleQueryRequest request) {
        List<RoleEntity> entities = roleService.list(request);
        List<RoleResponse> response = Optional.ofNullable(entities)
                .map(l -> l.stream().map(BaseEntity::getId).toList())
                .map(ids -> ((RoleMapper) roleService.getBaseMapper()).selectTree(ids, TreeBuildMode.CHILD_AND_CURRENT))
                .map(l -> roleConvertor.tree(l))
                .map(l -> roleConvertor.entityToResponse(l))
                .orElse(new ArrayList<>());
        return new ListView<>(response);
    }

    @Override
    @PostMapping("/page")
    @PreAuthorize("hasAuthority('ROLE_SUMMARY')")
    public PageView<RoleResponse> page(@RequestBody @Valid PageRequest<RoleQueryRequest> request) {
        IPage<RoleEntity> page = roleService.page(PageHelper.toPage(new PageDTO<>(), request),
                Optional.ofNullable(request.getParams()).orElse(new RoleQueryRequest()));
        List<RoleResponse> response = Optional.ofNullable(page.getRecords())
                .map(l -> roleConvertor.entityToResponse(l))
                .orElse(new ArrayList<>());
        return new PageView<>(response, page.getTotal(), page.getCurrent(), page.getSize());
    }

}
