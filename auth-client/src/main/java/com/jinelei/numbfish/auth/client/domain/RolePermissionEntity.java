package com.jinelei.numbfish.auth.client.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jinelei.numbfish.auth.enumeration.RolePermissionType;

import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("unused")
@TableName("role_permission")
public class RolePermissionEntity extends BaseEntity<Long> {
    protected Long roleId;
    protected Long permissionId;
    protected RolePermissionType type;

    public boolean isSimilar(RolePermissionEntity other) {
        return Optional.ofNullable(roleId).equals(Optional.ofNullable(other).map(RolePermissionEntity::getRoleId))
                && Optional.ofNullable(permissionId).equals(Optional.ofNullable(other).map(RolePermissionEntity::getPermissionId));
    }

    public RolePermissionEntity cover(RolePermissionEntity other) {
        Optional.ofNullable(other).map(RolePermissionEntity::getType).ifPresent(this::setType);
        Optional.ofNullable(other).map(BaseEntity::getRemark).ifPresent(this::setRemark);
        return this;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public RolePermissionType getType() {
        return type;
    }

    public void setType(RolePermissionType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        RolePermissionEntity that = (RolePermissionEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(roleId, that.roleId)
                && Objects.equals(permissionId, that.permissionId) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, roleId, permissionId, type);
    }

    @Override
    public String toString() {
        return "RolePermissionEntity [roleId=" + roleId + ", permissionId=" + permissionId + ", type=" + type + ", id="
                + id + ", remark=" + remark + ", createdUserId=" + createdUserId + ", createdTime=" + createdTime
                + ", updatedUserId=" + updatedUserId + ", updatedTime=" + updatedTime + "]";
    }
}
