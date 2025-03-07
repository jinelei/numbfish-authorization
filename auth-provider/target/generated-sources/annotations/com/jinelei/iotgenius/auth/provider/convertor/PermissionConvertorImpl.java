package com.jinelei.iotgenius.auth.provider.convertor;

import com.jinelei.iotgenius.auth.dto.permission.PermissionRequest;
import com.jinelei.iotgenius.auth.provider.domain.PermissionEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-07T17:26:49+0800",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.41.0.z20250115-2156, environment: Java 21.0.5 (Eclipse Adoptium)"
)
@Component
public class PermissionConvertorImpl implements PermissionConvertor {

    @Override
    public PermissionEntity requestToEntity(PermissionRequest source) {
        if ( source == null ) {
            return null;
        }

        PermissionEntity permissionEntity = new PermissionEntity();

        permissionEntity.setCreatedTime( source.getCreatedTime() );
        permissionEntity.setCreatedUserId( source.getCreatedUserId() );
        permissionEntity.setDeletedTime( source.getDeletedTime() );
        permissionEntity.setDeletedUserId( source.getDeletedUserId() );
        permissionEntity.setEnabled( source.getEnabled() );
        permissionEntity.setRemark( source.getRemark() );
        permissionEntity.setUpdatedTime( source.getUpdatedTime() );
        permissionEntity.setUpdatedUserId( source.getUpdatedUserId() );
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
