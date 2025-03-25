package com.jinelei.iotgenius.auth.client.convertor;

import com.jinelei.iotgenius.auth.client.domain.UserEntity;
import com.jinelei.iotgenius.auth.dto.user.UserCreateRequest;
import com.jinelei.iotgenius.auth.dto.user.UserResponse;
import com.jinelei.iotgenius.auth.dto.user.UserUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@Mapper(componentModel = "spring")
public interface UserConvertor {

    @Mappings(value = {
            @Mapping(target = "roles", ignore = true),
            @Mapping(target = "permissions", ignore = true)
    })
    UserEntity entityFromCreateRequest(UserCreateRequest source);

    @Mappings(value = {
            @Mapping(target = "roles", ignore = true),
            @Mapping(target = "permissions", ignore = true)
    })
    UserEntity entityFromUpdateRequest(UserUpdateRequest source);

    @Mappings(value = {
            @Mapping(target = "roles", ignore = true),
            @Mapping(target = "permissions", ignore = true)
    })
    UserResponse entityToResponse(UserEntity source);
}
