package com.jinelei.numbfish.auth.convertor;

import com.jinelei.numbfish.auth.entity.UserEntity;
import com.jinelei.numbfish.auth.dto.user.UserCreateRequest;
import com.jinelei.numbfish.auth.dto.user.UserResponse;
import com.jinelei.numbfish.auth.dto.user.UserUpdateRequest;
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
}
