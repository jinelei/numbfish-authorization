package com.jinelei.iotgenius.auth.client.convertor;

import com.jinelei.iotgenius.auth.client.domain.RoleEntity;
import com.jinelei.iotgenius.auth.dto.role.RoleCreateRequest;
import com.jinelei.iotgenius.auth.dto.role.RoleResponse;
import com.jinelei.iotgenius.auth.dto.role.RoleUpdateRequest;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@Mapper(componentModel = "spring")
public interface RoleConvertor {
    RoleEntity entityFromCreateRequest(RoleCreateRequest source);

    RoleEntity entityFromUpdateRequest(RoleUpdateRequest source);

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
