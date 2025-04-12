package com.jinelei.numbfish.auth.client.controller;

import java.util.List;
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
import com.jinelei.numbfish.auth.api.client.ClientApi;
import com.jinelei.numbfish.auth.client.domain.ClientEntity;
import com.jinelei.numbfish.auth.client.helper.PageHelper;
import com.jinelei.numbfish.auth.client.service.ClientService;
import com.jinelei.numbfish.auth.dto.client.ClientCreateRequest;
import com.jinelei.numbfish.auth.dto.client.ClientDeleteRequest;
import com.jinelei.numbfish.auth.dto.client.ClientQueryRequest;
import com.jinelei.numbfish.auth.dto.client.ClientResponse;
import com.jinelei.numbfish.auth.dto.client.ClientUpdateRequest;
import com.jinelei.numbfish.common.request.PageRequest;
import com.jinelei.numbfish.common.view.BaseView;
import com.jinelei.numbfish.common.view.ListView;
import com.jinelei.numbfish.common.view.PageView;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

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
        clientService.create(request);
        return new BaseView<>("创建成功");
    }

    @Override
    @ApiOperationSupport(order = 2)
    @Operation(summary = "删除客户端")
    @PostMapping("/delete")
    public BaseView<Void> delete(@RequestBody @Valid ClientDeleteRequest request) {
        clientService.delete(request);
        return new BaseView<>("删除成功");
    }

    @Override
    @ApiOperationSupport(order = 3)
    @Operation(summary = "更新客户端")
    @PostMapping("/update")
    public BaseView<Void> update(@RequestBody @Valid ClientUpdateRequest request) {
        clientService.update(request);
        return new BaseView<>("更新成功");
    }

    @Override
    @ApiOperationSupport(order = 4)
    @Operation(summary = "获取客户端")
    @PostMapping("/get")
    public BaseView<ClientResponse> get(@RequestBody @Valid ClientQueryRequest request) {
        ClientEntity entity = clientService.get(request);
        ClientResponse convert = clientService.convert(entity);
        return new BaseView<>(convert);
    }

    @Override
    @ApiOperationSupport(order = 5)
    @Operation(summary = "获取客户端列表")
    @PostMapping("/list")
    public ListView<ClientResponse> list(@RequestBody @Valid ClientQueryRequest request) {
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
        IPage<ClientEntity> page = clientService.page(PageHelper.toPage(new PageDTO<>(), request), request.getParams());
        List<ClientResponse> collect = page.getRecords().parallelStream().map(entity -> clientService.convert(entity))
                .collect(Collectors.toList());
        return new PageView<>(collect, page.getTotal(), page.getPages(), page.getSize());
    }

}
