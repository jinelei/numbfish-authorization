package com.jinelei.numbfish.authorization.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.Objects;

@SuppressWarnings("unused")
@Schema(description = "用户更新密码请求对象")
public class UserUpdatePasswordRequest implements Serializable {
    @NotBlank(message = "用户名称不能为空")
    @Schema(description = "用户名称")
    private String username;
    @Schema(description = "密码")
    private String password;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserUpdatePasswordRequest that = (UserUpdatePasswordRequest) o;
        return Objects.equals(username, that.username) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

    @Override
    public String toString() {
        return "UserForgetPasswordRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}