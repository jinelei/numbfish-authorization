package com.jinelei.iotgenius.auth.client.convertor;

import com.jinelei.iotgenius.auth.client.domain.PermissionEntity;
import com.jinelei.iotgenius.auth.dto.permission.PermissionCreateRequest;
import com.jinelei.iotgenius.auth.dto.permission.PermissionResponse;
import com.jinelei.iotgenius.auth.dto.permission.PermissionUpdateRequest;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-08T22:31:24+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.13 (Azul Systems, Inc.)"
)
@Component
public class PermissionConvertorImpl implements PermissionConvertor {

    @Override
    public PermissionEntity entityFromCreateRequest(PermissionCreateRequest source) {
        if ( source == null ) {
            return null;
        }

        PermissionEntity permissionEntity = new PermissionEntity();

        permissionEntity.setRemark( source.getRemark() );
        permissionEntity.setName( source.getName() );
        permissionEntity.setCode( source.getCode() );
        permissionEntity.setSortValue( source.getSortValue() );
        permissionEntity.setParentId( source.getParentId() );

        return permissionEntity;
    }

    @Override
    public PermissionEntity entityFromUpdateRequest(PermissionUpdateRequest source) {
        if ( source == null ) {
            return null;
        }

        PermissionEntity permissionEntity = new PermissionEntity();

        permissionEntity.setId( source.getId() );
        permissionEntity.setRemark( source.getRemark() );
        permissionEntity.setName( source.getName() );
        permissionEntity.setCode( source.getCode() );
        permissionEntity.setSortValue( source.getSortValue() );
        permissionEntity.setParentId( source.getParentId() );

        return permissionEntity;
    }

    @Override
    public PermissionResponse entityToResponse(PermissionEntity source) {
        if ( source == null ) {
            return null;
        }

        PermissionResponse permissionResponse = new PermissionResponse();

        permissionResponse.setId( source.getId() );
        permissionResponse.setName( source.getName() );
        permissionResponse.setCode( source.getCode() );
        permissionResponse.setSortValue( source.getSortValue() );
        permissionResponse.setParentId( source.getParentId() );
        permissionResponse.setRemark( source.getRemark() );
        permissionResponse.setDeleted( source.getDeleted() );
        permissionResponse.setCreatedUserId( source.getCreatedUserId() );
        permissionResponse.setCreatedTime( source.getCreatedTime() );
        permissionResponse.setUpdatedUserId( source.getUpdatedUserId() );
        permissionResponse.setUpdatedTime( source.getUpdatedTime() );
        permissionResponse.setDeletedUserId( source.getDeletedUserId() );
        permissionResponse.setDeletedTime( source.getDeletedTime() );
        permissionResponse.setChildren( permissionEntityListToPermissionResponseList( source.getChildren() ) );

        return permissionResponse;
    }

    protected List<PermissionResponse> permissionEntityListToPermissionResponseList(List<PermissionEntity> list) {
        if ( list == null ) {
            return null;
        }

        List<PermissionResponse> list1 = new ArrayList<PermissionResponse>( list.size() );
        for ( PermissionEntity permissionEntity : list ) {
            list1.add( entityToResponse( permissionEntity ) );
        }

        return list1;
    }
}
