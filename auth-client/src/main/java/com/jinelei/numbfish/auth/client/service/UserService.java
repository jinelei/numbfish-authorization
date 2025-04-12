package com.jinelei.numbfish.auth.client.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jinelei.numbfish.auth.client.domain.UserEntity;
import com.jinelei.numbfish.auth.dto.user.*;

import java.util.List;
import java.util.function.Function;

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

}
