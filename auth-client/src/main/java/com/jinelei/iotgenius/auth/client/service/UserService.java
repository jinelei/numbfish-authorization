package com.jinelei.iotgenius.auth.client.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jinelei.iotgenius.auth.client.domain.UserEntity;
import com.jinelei.iotgenius.auth.dto.user.*;

import java.util.List;

public interface UserService extends IService<UserEntity> {

    void create(UserCreateRequest request);

    void delete(UserDeleteRequest request);

    void update(UserUpdateRequest request);

    UserEntity get(UserQueryRequest request);

    List<UserEntity> list(UserQueryRequest request);

    IPage<UserEntity> page(IPage<UserEntity> page, UserQueryRequest request);

    UserResponse convert(UserEntity entity);

}
