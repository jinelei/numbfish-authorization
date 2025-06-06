package com.jinelei.numbfish.authorization.convertor;

import com.jinelei.numbfish.authorization.entity.ClientEntity;
import com.jinelei.numbfish.authorization.dto.ClientCreateRequest;
import com.jinelei.numbfish.authorization.dto.ClientResponse;
import com.jinelei.numbfish.authorization.dto.ClientUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@SuppressWarnings("unused")
@Mapper(componentModel = "spring")
public interface ClientConvertor {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdTime", ignore = true),
            @Mapping(target = "createdUserId", ignore = true),
            @Mapping(target = "updatedTime", ignore = true),
            @Mapping(target = "updatedUserId", ignore = true),
            @Mapping(target = "permissions", ignore = true),
    })
    ClientEntity entityFromCreateRequest(ClientCreateRequest source);

    @Mappings(value = {
            @Mapping(target = "createdTime", ignore = true),
            @Mapping(target = "createdUserId", ignore = true),
            @Mapping(target = "updatedTime", ignore = true),
            @Mapping(target = "updatedUserId", ignore = true),
            @Mapping(target = "permissions", ignore = true),
    })
    ClientEntity entityFromUpdateRequest(ClientUpdateRequest source);

    @Mappings(value = {
            @Mapping(target = "permissions", ignore = true)
    })
    ClientResponse entityToResponse(ClientEntity source);
}
