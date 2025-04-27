package com.jinelei.numbfish.auth.controller;

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
import com.jinelei.numbfish.auth.api.ClientApi;
import com.jinelei.numbfish.auth.entity.ClientEntity;
import com.jinelei.numbfish.common.helper.PageHelper;
import com.jinelei.numbfish.auth.service.ClientService;
import com.jinelei.numbfish.auth.dto.ClientCreateRequest;
import com.jinelei.numbfish.auth.dto.ClientDeleteRequest;
import com.jinelei.numbfish.auth.dto.ClientQueryRequest;
import com.jinelei.numbfish.auth.dto.ClientResponse;
import com.jinelei.numbfish.auth.dto.ClientUpdateRequest;
import com.jinelei.numbfish.common.request.PageRequest;
import com.jinelei.numbfish.common.view.BaseView;
import com.jinelei.numbfish.common.view.ListView;
import com.jinelei.numbfish.common.view.PageView;

import jakarta.validation.Valid;

@SuppressWarnings("unused")
@Validated
@RestController
@RequestMapping("/client")
public class ClientController implements ClientApi {

    @Autowired
    protected ClientService clientService;

    @Override
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('CLIENT_CREATE')")
    public BaseView<Void> create(@RequestBody @Valid ClientCreateRequest request) {
        clientService.create(request);
        return new BaseView<>("创建成功");
    }

    @Override
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('CLIENT_DELETE')")
    public BaseView<Void> delete(@RequestBody @Valid ClientDeleteRequest request) {
        clientService.delete(request);
        return new BaseView<>("删除成功");
    }

    @Override
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('CLIENT_UPDATE')")
    public BaseView<Void> update(@RequestBody @Valid ClientUpdateRequest request) {
        clientService.update(request);
        return new BaseView<>("更新成功");
    }

    @Override
    @PostMapping("/get")
    @PreAuthorize("hasAuthority('CLIENT_DETAIL')")
    public BaseView<ClientResponse> get(@RequestBody @Valid ClientQueryRequest request) {
        ClientEntity entity = clientService.get(request);
        ClientResponse convert = clientService.convert(entity);
        return new BaseView<>(convert);
    }

    @Override
    @PostMapping("/list")
    @PreAuthorize("hasAuthority('CLIENT_SUMMARY')")
    public ListView<ClientResponse> list(@RequestBody @Valid ClientQueryRequest request) {
        List<ClientEntity> entities = clientService.list(request);
        List<ClientResponse> convert = entities.parallelStream().map(entity -> clientService.convert(entity))
                .collect(Collectors.toList());
        return new ListView<>(convert);
    }

    @Override
    @PostMapping("/page")
    @PreAuthorize("hasAuthority('CLIENT_SUMMARY')")
    public PageView<ClientResponse> page(@RequestBody @Valid PageRequest<ClientQueryRequest> request) {
        IPage<ClientEntity> page = clientService.page(PageHelper.toPage(new PageDTO<>(), request),
                Optional.ofNullable(request.getParams()).orElse(new ClientQueryRequest()));
        List<ClientResponse> collect = page.getRecords().parallelStream().map(entity -> clientService.convert(entity))
                .collect(Collectors.toList());
        return new PageView<>(collect, page.getTotal(), page.getPages(), page.getSize());
    }

}
