package com.jinelei.numbfish.authorization.convertor;

import com.jinelei.numbfish.authorization.dto.PermissionCreateRequest;
import com.jinelei.numbfish.authorization.dto.PermissionResponse;
import com.jinelei.numbfish.authorization.dto.PermissionUpdateRequest;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import com.jinelei.numbfish.authorization.entity.PermissionEntity;

import java.util.*;

@SuppressWarnings("unused")
@Mapper(componentModel = "spring")
public interface PermissionConvertor {
    /**
     * 构建树形结构
     *
     * @param allPermissions 所有权限列表
     * @return 树形结构的权限列表
     */
    default List<PermissionEntity> tree(List<PermissionEntity> allPermissions) {
        if (Objects.isNull(allPermissions)) {
            return null;
        }
        if (allPermissions.isEmpty()) {
            return new ArrayList<>();
        }
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

    /**
     * 请求对象转换为实体对象
     *
     * @param source 请求对象
     * @return 实体对象
     */
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "children", ignore = true),
            @Mapping(target = "createdTime", ignore = true),
            @Mapping(target = "createdUserId", ignore = true),
            @Mapping(target = "updatedTime", ignore = true),
            @Mapping(target = "updatedUserId", ignore = true),
    })
    PermissionEntity requestToEntity(PermissionCreateRequest source);

    /**
     * 请求对象转换为实体对象
     *
     * @param source 请求对象
     * @return 实体对象
     */
    @Mappings({
            @Mapping(target = "children", ignore = true),
            @Mapping(target = "createdTime", ignore = true),
            @Mapping(target = "createdUserId", ignore = true),
            @Mapping(target = "updatedTime", ignore = true),
            @Mapping(target = "updatedUserId", ignore = true),
    })
    PermissionEntity requestToEntity(PermissionUpdateRequest source);

    /**
     * 实体对象列表转换为响应对象列表
     *
     * @param entities 实体对象列表
     * @return 响应对象列表
     */
    List<PermissionResponse> entityToResponse(List<PermissionEntity> entities);

    /**
     * 实体对象转换为响应对象
     *
     * @param source 实体对象
     * @return 响应对象
     */
    @Mappings({
            @Mapping(target = "id", source = "source.id"),
            @Mapping(target = "name", source = "source.name"),
            @Mapping(target = "code", source = "source.code"),
            @Mapping(target = "type", source = "source.type"),
            @Mapping(target = "sortValue", source = "source.sortValue"),
            @Mapping(target = "parentId", source = "source.parentId"),
            @Mapping(target = "remark", source = "source.remark"),
            @Mapping(target = "createdUserId", source = "source.createdUserId"),
            @Mapping(target = "createdTime", source = "source.createdTime"),
            @Mapping(target = "updatedUserId", source = "source.updatedUserId"),
            @Mapping(target = "updatedTime", source = "source.updatedTime"),
            @Mapping(target = "children", expression = "java(entityToResponse(source.getChildren()))")
    })
    PermissionResponse entityToResponse(PermissionEntity source);

}
