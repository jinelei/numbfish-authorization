package com.jinelei.numbfish.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jinelei.numbfish.common.entity.BaseEntity;

import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("unused")
@TableName("client_permission")
public class ClientPermissionEntity extends BaseEntity<Long> {
    protected Long clientId;
    protected Long permissionId;

    public boolean isSimilar(ClientPermissionEntity other) {
        return Optional.ofNullable(clientId).equals(Optional.ofNullable(other).map(ClientPermissionEntity::getClientId))
                && Optional.ofNullable(permissionId).equals(Optional.ofNullable(other).map(ClientPermissionEntity::getPermissionId));
    }

    public ClientPermissionEntity cover(ClientPermissionEntity other) {
        Optional.ofNullable(other).map(BaseEntity::getRemark).ifPresent(this::setRemark);
        return this;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
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
        ClientPermissionEntity that = (ClientPermissionEntity) o;
        return Objects.equals(clientId, that.clientId) && Objects.equals(permissionId, that.permissionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), clientId, permissionId);
    }

    @Override
    public String toString() {
        return "ClientPermissionEntity{" +
                "clientId=" + clientId +
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