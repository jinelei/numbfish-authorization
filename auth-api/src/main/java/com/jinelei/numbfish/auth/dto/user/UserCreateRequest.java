package com.jinelei.numbfish.auth.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
@Schema(description = "用户创建请求对象")
public class UserCreateRequest implements Serializable {
    @NotBlank(message = "用户名称不能为空")
    @Schema(description = "用户名称")
    private String username;
    @Schema(description = "密码")
    private String password;
    @Schema(description = "用户头像")
    private String avatar;
    @Schema(description = "邮箱")
    private String email;
    @Schema(description = "手机号")
    private String phone;
    @Schema(description = "用户备注")
    protected String remark;
    @NotNull(message = "角色列表不能为空")
    @Schema(description = "角色列表")
    protected List<Long> roleIds;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserCreateRequest that = (UserCreateRequest) o;
        return Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(avatar, that.avatar) && Objects.equals(email, that.email) && Objects.equals(phone, that.phone) && Objects.equals(remark, that.remark) && Objects.equals(roleIds, that.roleIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, avatar, email, phone, remark, roleIds);
    }

    @Override
    public String toString() {
        return "UserCreateRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", avatar='" + avatar + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", remark='" + remark + '\'' +
                ", roleIds=" + roleIds +
                '}';
    }
}
