package com.jinelei.numbfish.authorization.convertor;

import com.jinelei.numbfish.authorization.dto.UserInfoResponse;
import com.jinelei.numbfish.authorization.entity.UserEntity;
import com.jinelei.numbfish.authorization.dto.SetupRequest;
import com.jinelei.numbfish.authorization.dto.UserCreateRequest;
import com.jinelei.numbfish.authorization.dto.UserResponse;
import com.jinelei.numbfish.authorization.dto.UserUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@SuppressWarnings("unused")
@Mapper(componentModel = "spring")
public interface UserConvertor {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdTime", ignore = true),
            @Mapping(target = "createdUserId", ignore = true),
            @Mapping(target = "updatedTime", ignore = true),
            @Mapping(target = "updatedUserId", ignore = true),
            @Mapping(target = "roles", ignore = true),
            @Mapping(target = "permissions", ignore = true),
    })
    UserEntity entityFromCreateRequest(UserCreateRequest source);

    @Mappings(value = {
            @Mapping(target = "createdTime", ignore = true),
            @Mapping(target = "createdUserId", ignore = true),
            @Mapping(target = "updatedTime", ignore = true),
            @Mapping(target = "updatedUserId", ignore = true),
            @Mapping(target = "roles", ignore = true),
            @Mapping(target = "permissions", ignore = true),
    })
    UserEntity entityFromUpdateRequest(UserUpdateRequest source);

    @Mappings(value = {
            @Mapping(target = "roles", ignore = true),
            @Mapping(target = "roleIds", ignore = true),
            @Mapping(target = "permissions", ignore = true)
    })
    UserResponse entityToResponse(UserEntity source);

    @Mappings(value = {
            @Mapping(target = "phone", ignore = true),
            @Mapping(target = "email", ignore = true),
            @Mapping(target = "roles", ignore = true),
            @Mapping(target = "permissions", ignore = true)
    })
    UserInfoResponse entityToInfoResponse(UserEntity source);

    @Mappings(value = {
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "roleIds", ignore = true)
    })
    UserUpdateRequest entityFromSetupRequest(SetupRequest request);
}
