package com.jinelei.iotgenius.auth.client.convertor;

import com.jinelei.iotgenius.auth.dto.permission.PermissionCreateRequest;
import com.jinelei.iotgenius.auth.dto.permission.PermissionResponse;
import com.jinelei.iotgenius.auth.dto.permission.PermissionUpdateRequest;
import org.mapstruct.Mapper;

import com.jinelei.iotgenius.auth.client.domain.PermissionEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@Mapper(componentModel = "spring")
public interface PermissionConvertor {
    PermissionEntity entityFromCreateRequest(PermissionCreateRequest source);

    PermissionEntity entityFromUpdateRequest(PermissionUpdateRequest source);

    PermissionResponse entityToResponse(PermissionEntity source);

    default List<PermissionEntity> tree(List<PermissionEntity> allPermissions) {
        Map<Long, PermissionEntity> permissionMap = new HashMap<>();
        List<PermissionEntity> rootPermissions = new ArrayList<>();

        // 将所有权限添加到映射中
        for (PermissionEntity permission : allPermissions) {
            permissionMap.put(permission.getId(), permission);
        }

        // 构建树形结构
        for (PermissionEntity permission : allPermissions) {
            Long parentId = permission.getParentId();
            if (parentId == null || parentId == 0) {
                rootPermissions.add(permission);
            } else {
                PermissionEntity parent = permissionMap.get(parentId);
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    if (!parent.getChildren().contains(permission)) {
                        parent.getChildren().add(permission);
                    }
                }
            }
        }

        return rootPermissions;
    }
}
