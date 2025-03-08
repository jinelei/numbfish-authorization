package com.jinelei.iotgenius.auth.provider.convertor;

import com.jinelei.iotgenius.auth.dto.permission.PermissionRequest;
import com.jinelei.iotgenius.auth.provider.domain.PermissionEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-08T12:49:57+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.13 (Azul Systems, Inc.)"
)
@Component
public class PermissionConvertorImpl implements PermissionConvertor {

    @Override
    public PermissionEntity requestToEntity(PermissionRequest source) {
        if ( source == null ) {
            return null;
        }

        PermissionEntity permissionEntity = new PermissionEntity();

        permissionEntity.setRemark( source.getRemark() );
        permissionEntity.setEnabled( source.getEnabled() );
        permissionEntity.setCreatedUserId( source.getCreatedUserId() );
        permissionEntity.setCreatedTime( source.getCreatedTime() );
        permissionEntity.setUpdatedUserId( source.getUpdatedUserId() );
        permissionEntity.setUpdatedTime( source.getUpdatedTime() );
        permissionEntity.setDeletedUserId( source.getDeletedUserId() );
        permissionEntity.setDeletedTime( source.getDeletedTime() );
        permissionEntity.setId( source.getId() );
        permissionEntity.setName( source.getName() );
        permissionEntity.setCode( source.getCode() );
        permissionEntity.setSortValue( source.getSortValue() );
        if ( source.getParentId() != null ) {
            permissionEntity.setParentId( Long.parseLong( source.getParentId() ) );
        }

        return permissionEntity;
    }
}
