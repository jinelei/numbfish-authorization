package com.jinelei.numbfish.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
@Schema(description = "用户信息响应对象")
public class UserInfoResponse implements Serializable {
    @Schema(description = "id")
    protected Long id;
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "手机号")
    private String phone;
    @Schema(description = "邮箱")
    private String email;
    @Schema(description = "备注")
    private String remark;
    @Schema(description = "权限列表")
    protected List<PermissionResponse> permissions;
    @Schema(description = "角色列表")
    protected List<RoleResponse> roles;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<PermissionResponse> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionResponse> permissions) {
        this.permissions = permissions;
    }

    public List<RoleResponse> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleResponse> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserInfoResponse that = (UserInfoResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username) && Objects.equals(phone, that.phone) && Objects.equals(email, that.email) && Objects.equals(remark, that.remark) && Objects.equals(permissions, that.permissions) && Objects.equals(roles, that.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, phone, email, remark, permissions, roles);
    }

    @Override
    public String toString() {
        return "UserInfoResponse{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", remark='" + remark + '\'' +
                ", permissions=" + permissions +
                ", roles=" + roles +
                '}';
    }
}