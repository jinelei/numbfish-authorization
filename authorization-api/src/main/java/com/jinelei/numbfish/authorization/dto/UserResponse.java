package com.jinelei.numbfish.authorization.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
@Schema(description = "用户响应对象")
public class UserResponse implements Serializable {
    @Schema(description = "用户实体的唯一标识")
    private Long id;
    @Schema(description = "用户名称")
    private String username;
    @Schema(description = "用户昵称")
    private String nickname;
    @Schema(description = "邮箱")
    private String email;
    @Schema(description = "手机号")
    private String phone;
    @Schema(description = "用户头像")
    private String avatar;
    @Schema(description = "用户备注")
    protected String remark;
    @Schema(description = "角色id列表")
    protected List<Long> roleIds;
    @Schema(description = "角色实体列表")
    protected List<RoleResponse> roles;
    @Schema(description = "权限id列表")
    protected List<Long> permissionIds;
    @Schema(description = "权限列表")
    protected List<PermissionResponse> permissions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public List<RoleResponse> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleResponse> roles) {
        this.roles = roles;
    }

    public List<Long> getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(List<Long> permissionIds) {
        this.permissionIds = permissionIds;
    }

    public List<PermissionResponse> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionResponse> permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserResponse that = (UserResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username) && Objects.equals(nickname, that.nickname) && Objects.equals(email, that.email) && Objects.equals(phone, that.phone) && Objects.equals(avatar, that.avatar) && Objects.equals(remark, that.remark) && Objects.equals(roleIds, that.roleIds) && Objects.equals(roles, that.roles) && Objects.equals(permissionIds, that.permissionIds) && Objects.equals(permissions, that.permissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, nickname, email, phone, avatar, remark, roleIds, roles, permissionIds, permissions);
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", avatar='" + avatar + '\'' +
                ", remark='" + remark + '\'' +
                ", roleIds=" + roleIds +
                ", roles=" + roles +
                ", permissionIds=" + permissionIds +
                ", permissions=" + permissions +
                '}';
    }
}
