package com.jinelei.iotgenius.auth.client.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jinelei.iotgenius.auth.client.domain.ClientEntity;
import com.jinelei.iotgenius.auth.dto.client.*;

import java.util.List;
import java.util.function.Function;

@SuppressWarnings("unused")
public interface ClientService extends IService<ClientEntity> {
    String PASSWORD = "123456";
    Function<String, String> GENERATE_TOKEN_INFO = s -> "Client:token:info:" + s;
    Function<String, String> CACHED_ROLE_ID_TOKEN = s -> "Client:role:id:" + s;
    Function<String, String> CACHED_PERMISSION_ID_TOKEN = s -> "Client:permissions:id:" + s;

    void create(ClientCreateRequest request);

    void delete(ClientDeleteRequest request);

    void update(ClientUpdateRequest request);

    ClientEntity get(ClientQueryRequest request);

    List<ClientEntity> list(ClientQueryRequest request);

    IPage<ClientEntity> page(IPage<ClientEntity> page, ClientQueryRequest request);

    ClientResponse convert(ClientEntity entity);

}
