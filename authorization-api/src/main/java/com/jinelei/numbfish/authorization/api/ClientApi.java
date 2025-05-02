package com.jinelei.numbfish.authorization.api;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.jinelei.numbfish.authorization.dto.*;
import com.jinelei.numbfish.common.request.PageRequest;
import com.jinelei.numbfish.common.view.BaseView;
import com.jinelei.numbfish.common.view.ListView;
import com.jinelei.numbfish.common.view.PageView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/*
 * @Author: jinelei
 * @Date: 2025-02-28 16:08:10
 * @LastEditTime: 2025-02-28 16:09:00
 * @Description: ClientApi
 */
@ApiSupport(order = 3)
@Tag(name = "客户端管理")
@SuppressWarnings("unused")
public interface ClientApi {
    /**
     * 创建客户端
     *
     * @param request 客户端请求对象
     * @return 客户端响应对象
     */
    @ApiOperationSupport(order = 1)
    @Operation(summary = "创建客户端")
    BaseView<Void> create(@Valid ClientCreateRequest request);

    /**
     * 删除客户端
     *
     * @param request 客户端请求对象
     */
    @ApiOperationSupport(order = 2)
    @Operation(summary = "删除客户端")
    BaseView<Void> delete(@Valid ClientDeleteRequest request);

    /**
     * 更新客户端
     *
     * @param request 客户端请求对象
     * @return 客户端响应对象
     */
    @ApiOperationSupport(order = 3)
    @Operation(summary = "更新客户端")
    BaseView<Void> update(@Valid ClientUpdateRequest request);

    /**
     * 查询客户端详情
     *
     * @param request 客户端请求对象
     * @return 客户端响应对象
     */
    @ApiOperationSupport(order = 4)
    @Operation(summary = "获取客户端")
    BaseView<ClientResponse> get(@Valid ClientQueryRequest request);

    /**
     * 查询客户端列表
     *
     * @param request 客户端请求对象
     * @return 客户端响应对象列表
     */
    @ApiOperationSupport(order = 5)
    @Operation(summary = "获取客户端列表")
    ListView<ClientResponse> list(@Valid ClientQueryRequest request);

    /**
     * 查询客户端分页
     *
     * @param request 客户端请求对象
     * @return 客户端响应对象列表
     */
    @ApiOperationSupport(order = 6)
    @Operation(summary = "获取客户端分页")
    PageView<ClientResponse> page(@Valid PageRequest<ClientQueryRequest> request);

}