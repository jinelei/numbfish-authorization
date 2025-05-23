package com.jinelei.numbfish.authorization.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jinelei.numbfish.common.entity.BaseEntity;

import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("unused")
@TableName("role_permission")
public class RolePermissionEntity extends BaseEntity<Long> {
    protected Long roleId;
    protected Long permissionId;

    public boolean isSimilar(RolePermissionEntity other) {
        return Optional.ofNullable(roleId).equals(Optional.ofNullable(other).map(RolePermissionEntity::getRoleId))
                && Optional.ofNullable(permissionId).equals(Optional.ofNullable(other).map(RolePermissionEntity::getPermissionId));
    }

    public RolePermissionEntity cover(RolePermissionEntity other) {
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RolePermissionEntity that = (RolePermissionEntity) o;
        return Objects.equals(roleId, that.roleId) && Objects.equals(permissionId, that.permissionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), roleId, permissionId);
    }

    @Override
    public String toString() {
        return "RolePermissionEntity{" +
                "roleId=" + roleId +
                ", permissionId=" + permissionId +
                ", id=" + id +
                ", remark='" + remark + '\'' +
                ", createdUserId=" + createdUserId +
                ", createdTime=" + createdTime +
                ", updatedUserId=" + updatedUserId +
                ", updatedTime=" + updatedTime +
                '}';
    }
}
