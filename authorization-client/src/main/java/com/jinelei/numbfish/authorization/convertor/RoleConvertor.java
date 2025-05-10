package com.jinelei.numbfish.authorization.convertor;

import com.jinelei.numbfish.authorization.dto.PermissionResponse;
import com.jinelei.numbfish.authorization.entity.PermissionEntity;
import com.jinelei.numbfish.authorization.entity.RoleEntity;
import com.jinelei.numbfish.authorization.dto.RoleCreateRequest;
import com.jinelei.numbfish.authorization.dto.RoleResponse;
import com.jinelei.numbfish.authorization.dto.RoleUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.*;

@SuppressWarnings("unused")
@Mapper(componentModel = "spring")
public interface RoleConvertor {
    /**
     * 构建树形结构
     *
     * @param allRoles 所有角色列表
     * @return 树形结构的角色列表
     */
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
            @Mapping(target = "blackPermissions", ignore = true),
            @Mapping(target = "whitePermissions", ignore = true),
            @Mapping(target = "level", ignore = true),
            @Mapping(target = "ancestor", ignore = true),
    })
    RoleEntity requestToEntity(RoleCreateRequest source);

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
            @Mapping(target = "blackPermissions", ignore = true),
            @Mapping(target = "whitePermissions", ignore = true),
            @Mapping(target = "level", ignore = true),
            @Mapping(target = "ancestor", ignore = true),
    })
    RoleEntity requestToEntity(RoleUpdateRequest source);

    /**
     * 实体对象列表转换为响应对象列表
     *
     * @param entities 实体对象列表
     * @return 响应对象列表
     */
    List<RoleResponse> entityToResponse(List<RoleEntity> entities);

    /**
     * 实体对象转换为响应对象
     *
     * @param source 实体对象
     * @return 响应对象
     */
    @Mappings({
            @Mapping(target = "deleted", ignore = true),
            @Mapping(target = "deletedTime", ignore = true),
            @Mapping(target = "deletedUserId", ignore = true),
            @Mapping(target = "blackPermissionIds", ignore = true),
            @Mapping(target = "whitePermissionIds", ignore = true),
    })
    RoleResponse entityToResponse(RoleEntity source);

    default RoleResponse coverPermissionResponse(RoleResponse response, List<PermissionResponse> permissions) {
        response.setWhitePermissions(permissions);
        return response;
    }

}
