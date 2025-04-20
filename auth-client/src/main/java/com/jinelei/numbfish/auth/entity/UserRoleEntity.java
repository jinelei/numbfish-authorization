package com.jinelei.numbfish.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jinelei.numbfish.common.entity.BaseEntity;

import java.util.Objects;

@SuppressWarnings("unused")
@TableName("user_role")
public class UserRoleEntity extends BaseEntity<Long> {
    protected Long userId;
    protected Long roleId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserRoleEntity that = (UserRoleEntity) o;
        return Objects.equals(userId, that.userId) && Objects.equals(roleId, that.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId, roleId);
    }

    @Override
    public String toString() {
        return "UserRoleEntity [userId=" + userId + ", roleId=" + roleId + ", id=" + id + ", remark=" + remark
                + ", createdUserId=" + createdUserId + ", createdTime=" + createdTime + ", updatedUserId="
                + updatedUserId + ", updatedTime=" + updatedTime + "]";
    }
}
