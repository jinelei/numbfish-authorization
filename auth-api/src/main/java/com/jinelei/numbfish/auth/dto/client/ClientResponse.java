package com.jinelei.numbfish.auth.dto.client;

import com.jinelei.numbfish.auth.dto.permission.PermissionResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
@Schema(description = "客户端响应对象")
public class ClientResponse implements Serializable {
    @Schema(description = "id")
    protected Long id;
    @Schema(description = "访问主键")
    private String accessKey;
    @Schema(description = "访问密钥")
    private String secretKey;
    @Schema(description = "过期时间")
    private LocalDateTime expiredAt;
    @Schema(description = "备注")
    private String remark;
    @Schema(description = "权限列表")
    protected List<PermissionResponse> permissions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<PermissionResponse> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionResponse> permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        ClientResponse that = (ClientResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(accessKey, that.accessKey)
                && Objects.equals(expiredAt, that.expiredAt) && Objects.equals(remark, that.remark)
                && Objects.equals(permissions, that.permissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accessKey, expiredAt, remark, permissions);
    }

    @Override
    public String toString() {
        return "ClientResponse{" +
                "id=" + id +
                ", accessKey='" + accessKey + '\'' +
                ", expiredAt=" + expiredAt +
                ", remark='" + remark + '\'' +
                ", permissions=" + permissions +
                '}';
    }
}