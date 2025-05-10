package com.jinelei.numbfish.authorization.dto;

import com.jinelei.numbfish.authorization.enumeration.PermissionType;
import com.jinelei.numbfish.common.response.TreeResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
@Schema(description = "权限响应对象")
public class PermissionResponse implements TreeResponse<PermissionResponse>, Serializable {
    @Schema(description = "权限实体的唯一标识")
    private Long id;
    @Schema(description = "权限名称")
    private String name;
    @Schema(description = "权限代码")
    private String code;
    @Schema(description = "权限类型")
    private PermissionType type;
    @Schema(description = "权限排序值")
    private Integer sortValue;
    @Schema(description = "父权限 ID")
    private Long parentId;
    @Schema(description = "权限备注信息")
    private String remark;
    @Schema(description = "创建人用户 ID")
    private Long createdUserId;
    @Schema(description = "创建时间")
    private LocalDateTime createdTime;
    @Schema(description = "更新人用户 ID")
    private Long updatedUserId;
    @Schema(description = "更新时间")
    private LocalDateTime updatedTime;
    @Schema(description = "子级列表")
    private List<PermissionResponse> children;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public PermissionType getType() {
        return type;
    }

    public void setType(PermissionType type) {
        this.type = type;
    }

    public Integer getSortValue() {
        return sortValue;
    }

    public void setSortValue(Integer sortValue) {
        this.sortValue = sortValue;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(Long createdUserId) {
        this.createdUserId = createdUserId;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public Long getUpdatedUserId() {
        return updatedUserId;
    }

    public void setUpdatedUserId(Long updatedUserId) {
        this.updatedUserId = updatedUserId;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public List<PermissionResponse> getChildren() {
        return children;
    }

    public void setChildren(List<PermissionResponse> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PermissionResponse that = (PermissionResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(code, that.code) && type == that.type && Objects.equals(sortValue, that.sortValue) && Objects.equals(parentId, that.parentId) && Objects.equals(remark, that.remark) && Objects.equals(createdUserId, that.createdUserId) && Objects.equals(createdTime, that.createdTime) && Objects.equals(updatedUserId, that.updatedUserId) && Objects.equals(updatedTime, that.updatedTime) && Objects.equals(children, that.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, type, sortValue, parentId, remark, createdUserId, createdTime, updatedUserId, updatedTime, children);
    }

    @Override
    public String toString() {
        return "PermissionResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", type=" + type +
                ", sortValue=" + sortValue +
                ", parentId=" + parentId +
                ", remark='" + remark + '\'' +
                ", createdUserId='" + createdUserId + '\'' +
                ", createdTime=" + createdTime +
                ", updatedUserId='" + updatedUserId + '\'' +
                ", updatedTime=" + updatedTime +
                ", children=" + children +
                '}';
    }
}
