package com.jinelei.numbfish.authorization.convertor;

import com.jinelei.numbfish.authorization.dto.PermissionCreateRequest;
import com.jinelei.numbfish.authorization.dto.PermissionResponse;
import com.jinelei.numbfish.authorization.dto.PermissionUpdateRequest;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.jinelei.numbfish.authorization.entity.PermissionEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@Mapper(componentModel = "spring")
public interface PermissionConvertor {
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "children", ignore = true),
            @Mapping(target = "createdTime", ignore = true),
            @Mapping(target = "createdUserId", ignore = true),
            @Mapping(target = "updatedTime", ignore = true),
            @Mapping(target = "updatedUserId", ignore = true),
    })
    PermissionEntity entityFromCreateRequest(PermissionCreateRequest source);

    @Mappings({
            @Mapping(target = "children", ignore = true),
            @Mapping(target = "createdTime", ignore = true),
            @Mapping(target = "createdUserId", ignore = true),
            @Mapping(target = "updatedTime", ignore = true),
            @Mapping(target = "updatedUserId", ignore = true),
    })
    PermissionEntity entityFromUpdateRequest(PermissionUpdateRequest source);

    @Mappings({
    })
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
            if (parentId == null || parentId == 0 || !permissionMap.containsKey(parentId)) {
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
