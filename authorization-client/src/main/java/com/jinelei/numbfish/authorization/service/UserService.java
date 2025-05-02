package com.jinelei.numbfish.authorization.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jinelei.numbfish.authorization.dto.*;
import com.jinelei.numbfish.authorization.entity.UserEntity;

import java.util.List;

@SuppressWarnings("unused")
public interface UserService extends IService<UserEntity> {
    String PASSWORD = "123456";

    void create(UserCreateRequest request);

    void delete(UserDeleteRequest request);

    void update(UserUpdateRequest request);

    UserEntity get(UserQueryRequest request);

    List<UserEntity> list(UserQueryRequest request);

    IPage<UserEntity> page(IPage<UserEntity> page, UserQueryRequest request);

    String login(UserLoginRequest request);

    void logout();

    void updatePassword(UserUpdatePasswordRequest request);

    UserResponse convert(UserEntity entity);

    UserInfoResponse info();
}
