package com.jinelei.numbfish.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
@Schema(description = "客户端创建请求对象")
public class ClientCreateRequest implements Serializable {
    @NotBlank(message = "访问主键不能为空")
    @Schema(description = "访问主键")
    private String accessKey;
    @NotBlank(message = "访问密钥不能为空")
    @Schema(description = "访问密钥")
    private String secretKey;
    @NotNull(message = "过期时间不能为空")
    @Schema(description = "过期时间")
    private LocalDateTime expiredAt;
    @Schema(description = "备注")
    private String remark;
    @Schema(description = "权限Id列表")
    protected List<Long> permissionIds;

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<Long> getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(List<Long> permissionIds) {
        this.permissionIds = permissionIds;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ClientCreateRequest that = (ClientCreateRequest) o;
        return Objects.equals(accessKey, that.accessKey) && Objects.equals(secretKey, that.secretKey) && Objects.equals(expiredAt, that.expiredAt) && Objects.equals(remark, that.remark) && Objects.equals(permissionIds, that.permissionIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessKey, secretKey, expiredAt, remark, permissionIds);
    }

    @Override
    public String toString() {
        return "ClientCreateRequest{" +
                "accessKey='" + accessKey + '\'' +
                ", secretKey='" + secretKey + '\'' +
                ", expiredAt=" + expiredAt +
                ", remark='" + remark + '\'' +
                ", permissionIds=" + permissionIds +
                '}';
    }
}