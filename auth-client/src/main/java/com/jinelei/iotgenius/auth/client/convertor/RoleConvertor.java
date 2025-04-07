package com.jinelei.iotgenius.auth.client.convertor;

import com.jinelei.iotgenius.auth.client.domain.RoleEntity;
import com.jinelei.iotgenius.auth.dto.role.RoleCreateRequest;
import com.jinelei.iotgenius.auth.dto.role.RoleResponse;
import com.jinelei.iotgenius.auth.dto.role.RoleUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@Mapper(componentModel = "spring")
public interface RoleConvertor {
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "children", ignore = true),
            @Mapping(target = "createdTime", ignore = true),
            @Mapping(target = "createdUserId", ignore = true),
            @Mapping(target = "updatedTime", ignore = true),
            @Mapping(target = "updatedUserId", ignore = true),
            @Mapping(target = "blackPermissions", ignore = true),
            @Mapping(target = "whitePermissions", ignore = true),
    })
    RoleEntity entityFromCreateRequest(RoleCreateRequest source);

    @Mappings({
            @Mapping(target = "children", ignore = true),
            @Mapping(target = "createdTime", ignore = true),
            @Mapping(target = "createdUserId", ignore = true),
            @Mapping(target = "updatedTime", ignore = true),
            @Mapping(target = "updatedUserId", ignore = true),
            @Mapping(target = "blackPermissions", ignore = true),
            @Mapping(target = "whitePermissions", ignore = true),
    })
    RoleEntity entityFromUpdateRequest(RoleUpdateRequest source);

    @Mappings({
            @Mapping(target = "deleted", ignore = true),
            @Mapping(target = "deletedTime", ignore = true),
            @Mapping(target = "deletedUserId", ignore = true),
            @Mapping(target = "blackPermissions", ignore = true),
            @Mapping(target = "blackPermissionIds", ignore = true),
            @Mapping(target = "whitePermissions", ignore = true),
            @Mapping(target = "whitePermissionIds", ignore = true),
    })
    RoleResponse entityToResponse(RoleEntity source);

    default List<RoleEntity> tree(List<RoleEntity> allRoles) {
        Map<Long, RoleEntity> roleMap = new HashMap<>();
        List<RoleEntity> rootRoles = new ArrayList<>();

        // 将所有角色添加到映射中
        for (RoleEntity role : allRoles) {
            roleMap.put(role.getId(), role);
        }

        // 构建树形结构
        for (RoleEntity role : allRoles) {
            Long parentId = role.getParentId();
            if (parentId == null || parentId == 0) {
                rootRoles.add(role);
            } else {
                RoleEntity parent = roleMap.get(parentId);
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    if (!parent.getChildren().contains(role)) {
                        parent.getChildren().add(role);
                    }
                }
            }
        }

        return rootRoles;
    }
}
