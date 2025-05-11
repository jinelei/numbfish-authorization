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

import java.util.List;

@SuppressWarnings("unused")
@Mapper(componentModel = "spring")
public interface UserConvertor {

    /**
     * 请求对象转换为实体对象
     *
     * @param source 请求对象
     * @return 实体对象
     */
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdTime", ignore = true),
            @Mapping(target = "createdUserId", ignore = true),
            @Mapping(target = "updatedTime", ignore = true),
            @Mapping(target = "updatedUserId", ignore = true),
            @Mapping(target = "permissionIds", ignore = true),
            @Mapping(target = "permissions", ignore = true),
    })
    UserEntity requestToEntity(UserCreateRequest source);

    /**
     * 请求对象转换为实体对象
     *
     * @param source 请求对象
     * @return 实体对象
     */
    @Mappings(value = {
            @Mapping(target = "createdTime", ignore = true),
            @Mapping(target = "createdUserId", ignore = true),
            @Mapping(target = "updatedTime", ignore = true),
            @Mapping(target = "updatedUserId", ignore = true),
            @Mapping(target = "permissionIds", ignore = true),
            @Mapping(target = "permissions", ignore = true),
    })
    UserEntity requestToEntity(UserUpdateRequest source);

    /**
     * 实体对象转换为响应对象
     *
     * @param source 实体对象
     * @return 响应对象
     */
    List<UserResponse> entityToResponse(List<UserEntity> source);

    /**
     * 实体对象转换为响应对象
     *
     * @param source 实体对象
     * @return 响应对象
     */
    @Mappings(value = {
            @Mapping(target = "permissionIds", ignore = true),
            @Mapping(target = "permissions", ignore = true)
    })
    UserResponse entityToResponse(UserEntity source);

    /**
     * 实体对象转换为响应对象
     *
     * @param source 实体对象
     * @return 响应对象
     */
    @Mappings(value = {
            @Mapping(target = "phone", ignore = true),
            @Mapping(target = "email", ignore = true),
            @Mapping(target = "permissionIds", ignore = true),
            @Mapping(target = "permissions", ignore = true)
    })
    UserInfoResponse entityToInfo(UserEntity source);

    /**
     * 实体对象转换为响应对象
     *
     * @param request 请求对象
     * @return 响应对象
     */
    @Mappings(value = {
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "password", ignore = true),
            @Mapping(target = "roleIds", ignore = true),
            @Mapping(target = "roles", ignore = true),
            @Mapping(target = "permissionIds", ignore = true),
            @Mapping(target = "permissions", ignore = true)
    })
    UserUpdateRequest setupToRequest(SetupRequest request);
}
