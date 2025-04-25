package com.jinelei.numbfish.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.Objects;

@SuppressWarnings("unused")
@Schema(description = "用户权限初始化请求对象")
public class SetupRequest implements Serializable {
    @NotBlank(message = "用户名称不能为空")
    @Schema(description = "用户名称")
    private String username;
    @NotBlank(message = "用户密码不能为空")
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SetupRequest that = (SetupRequest) o;
        return Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(avatar, that.avatar) && Objects.equals(email, that.email) && Objects.equals(phone, that.phone) && Objects.equals(remark, that.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, avatar, email, phone, remark);
    }

    @Override
    public String toString() {
        return "SetupRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", avatar='" + avatar + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}