package com.jinelei.numbfish.authorization.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jinelei.numbfish.common.entity.BaseEntity;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
@TableName("user")
public class UserEntity extends BaseEntity<Long> {
    protected String username;
    protected String password;
    protected String avatar;
    protected String email;
    protected String phone;
    @TableField(exist = false)
    private List<RoleEntity> roles;
    @TableField(exist = false)
    private List<PermissionEntity> permissions;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleEntity> roles) {
        this.roles = roles;
    }

    public List<PermissionEntity> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionEntity> permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(avatar, that.avatar) && Objects.equals(email, that.email) && Objects.equals(phone, that.phone) && Objects.equals(roles, that.roles) && Objects.equals(permissions, that.permissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), username, password, avatar, email, phone, roles, permissions);
    }

    @Override
    public String toString() {
        return "UserEntity [username=" + username + ", password=" + password + ", avatar=" + avatar + ", email=" + email
                + ", phone=" + phone + ", id=" + id + ", roles=" + roles + ", remark=" + remark + ", permissions="
                + permissions + ", createdUserId=" + createdUserId + ", createdTime=" + createdTime + ", updatedUserId="
                + updatedUserId + ", updatedTime=" + updatedTime + "]";
    }
}
