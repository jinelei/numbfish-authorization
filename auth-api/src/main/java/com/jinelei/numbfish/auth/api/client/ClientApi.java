package com.jinelei.numbfish.auth.api.client;

import com.jinelei.numbfish.auth.dto.client.*;
import com.jinelei.numbfish.common.request.PageRequest;
import com.jinelei.numbfish.common.view.BaseView;
import com.jinelei.numbfish.common.view.ListView;
import com.jinelei.numbfish.common.view.PageView;
import jakarta.validation.Valid;

/*
 * @Author: jinelei
 * @Date: 2025-02-28 16:08:10
 * @LastEditTime: 2025-02-28 16:09:00
 * @Description: ClientApi
 */
@SuppressWarnings("unused")
public interface ClientApi {
    /**
     * 创建客户端
     *
     * @param request 客户端请求对象
     * @return 客户端响应对象
     */
    BaseView<Void> create(@Valid ClientCreateRequest request);

    /**
     * 删除客户端
     *
     * @param request 客户端请求对象
     */
    BaseView<Void> delete(@Valid ClientDeleteRequest request);

    /**
     * 更新客户端
     *
     * @param request 客户端请求对象
     * @return 客户端响应对象
     */
    BaseView<Void> update(@Valid ClientUpdateRequest request);

    /**
     * 查询客户端详情
     *
     * @param request 客户端请求对象
     * @return 客户端响应对象
     */
    BaseView<ClientResponse> get(@Valid ClientQueryRequest request);

    /**
     * 查询客户端列表
     *
     * @param request 客户端请求对象
     * @return 客户端响应对象列表
     */
    ListView<ClientResponse> list(@Valid ClientQueryRequest request);

    /**
     * 查询客户端分页
     *
     * @param request 客户端请求对象
     * @return 客户端响应对象列表
     */
    PageView<ClientResponse> page(@Valid PageRequest<ClientQueryRequest> request);

}