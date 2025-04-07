package com.jinelei.iotgenius.auth.client.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.jinelei.iotgenius.auth.api.client.ClientApi;
import com.jinelei.iotgenius.auth.client.configuration.permission.instance.PermissionInstance;
import com.jinelei.iotgenius.auth.client.domain.ClientEntity;
import com.jinelei.iotgenius.auth.client.helper.PageHelper;
import com.jinelei.iotgenius.auth.client.service.ClientService;
import com.jinelei.iotgenius.auth.dto.client.*;
import com.jinelei.iotgenius.auth.helper.AuthorizationHelper;
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
@Tag(name = "客户端管理")
@Validated
@RestController
@RequestMapping("/client")
public class ClientController implements ClientApi {

    @Autowired
    protected ClientService clientService;

    @Override
    @ApiOperationSupport(order = 1)
    @Operation(summary = "创建客户端")
    @PostMapping("/create")
    public BaseView<Void> create(@RequestBody @Valid ClientCreateRequest request) {
        AuthorizationHelper.hasPermissions(PermissionInstance.CLIENT_CREATE);
        clientService.create(request);
        return new BaseView<>("创建成功");
    }

    @Override
    @ApiOperationSupport(order = 2)
    @Operation(summary = "删除客户端")
    @PostMapping("/delete")
    public BaseView<Void> delete(@RequestBody @Valid ClientDeleteRequest request) {
        AuthorizationHelper.hasPermissions(PermissionInstance.CLIENT_DELETE);
        clientService.delete(request);
        return new BaseView<>("删除成功");
    }

    @Override
    @ApiOperationSupport(order = 3)
    @Operation(summary = "更新客户端")
    @PostMapping("/update")
    public BaseView<Void> update(@RequestBody @Valid ClientUpdateRequest request) {
        AuthorizationHelper.hasPermissions(PermissionInstance.CLIENT_UPDATE);
        clientService.update(request);
        return new BaseView<>("更新成功");
    }

    @Override
    @ApiOperationSupport(order = 4)
    @Operation(summary = "获取客户端")
    @PostMapping("/get")
    public BaseView<ClientResponse> get(@RequestBody @Valid ClientQueryRequest request) {
        AuthorizationHelper.hasPermissions(PermissionInstance.CLIENT_DETAIL);
        ClientEntity entity = clientService.get(request);
        ClientResponse convert = clientService.convert(entity);
        return new BaseView<>(convert);
    }

    @Override
    @ApiOperationSupport(order = 5)
    @Operation(summary = "获取客户端列表")
    @PostMapping("/list")
    public ListView<ClientResponse> list(@RequestBody @Valid ClientQueryRequest request) {
        AuthorizationHelper.hasPermissions(PermissionInstance.CLIENT_SUMMARY);
        List<ClientEntity> entities = clientService.list(request);
        List<ClientResponse> convert = entities.parallelStream().map(entity -> clientService.convert(entity))
                .collect(Collectors.toList());
        return new ListView<>(convert);
    }

    @Override
    @ApiOperationSupport(order = 6)
    @Operation(summary = "获取客户端分页")
    @PostMapping("/page")
    public PageView<ClientResponse> page(@RequestBody @Valid PageRequest<ClientQueryRequest> request) {
        AuthorizationHelper.hasPermissions(PermissionInstance.CLIENT_SUMMARY);
        IPage<ClientEntity> page = clientService.page(PageHelper.toPage(new PageDTO<>(), request), request.getParams());
        List<ClientResponse> collect = page.getRecords().parallelStream().map(entity -> clientService.convert(entity))
                .collect(Collectors.toList());
        return new PageView<>(collect, page.getTotal(), page.getPages(), page.getSize());
    }

}
