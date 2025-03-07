package com.jinelei.iotgenius.auth.provider.convertor;

import org.mapstruct.Mapper;

import com.jinelei.iotgenius.auth.dto.permission.PermissionRequest;
import com.jinelei.iotgenius.auth.provider.domain.PermissionEntity;

@Mapper(componentModel = "spring")
public interface PermissionConvertor {

    PermissionEntity requestToEntity(PermissionRequest source);

}
