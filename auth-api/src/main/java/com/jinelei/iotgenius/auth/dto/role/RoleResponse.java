package com.jinelei.iotgenius.auth.dto.role;

import com.jinelei.iotgenius.auth.dto.permission.PermissionResponse;
import com.jinelei.iotgenius.auth.enums.RoleType;
import com.jinelei.iotgenius.common.response.TreeResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Schema(description = "角色响应对象")
public class RoleResponse implements TreeResponse<RoleResponse>, Serializable {
    @Schema(description = "角色实体的唯一标识")
    private Long id;
    @Schema(description = "角色名称")
    private String name;
    @Schema(description = "角色代码")
    private String code;
    @Schema(description = "角色类型")
    private RoleType type;
    @Schema(description = "角色排序值")
    private Integer sortValue;
    @Schema(description = "父角色 ID")
    private Long parentId;
    @Schema(description = "角色备注信息")
    private String remark;
    @Schema(description = "是否启用，0 表示启用，1 表示禁用")
    private Boolean deleted;
    @Schema(description = "创建人用户 ID")
    private String createdUserId;
    @Schema(description = "创建时间")
    private LocalDateTime createdTime;
    @Schema(description = "更新人用户 ID")
    private String updatedUserId;
    @Schema(description = "更新时间")
    private LocalDateTime updatedTime;
    @Schema(description = "删除人用户 ID")
    private String deletedUserId;
    @Schema(description = "删除时间")
    private LocalDateTime deletedTime;
    private List<RoleResponse> children;
    @Schema(description = "白名单权限列表")
    protected List<Long> whitePermissionIds;
    @Schema(description = "白名单权限实体列表")
    protected List<PermissionResponse> whitePermissions;
    @Schema(description = "黑名单权限列表")
    protected List<Long> blackPermissionIds;
    @Schema(description = "黑名单权限列表")
    protected List<PermissionResponse> blackPermissions;

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

    public RoleType getType() {
        return type;
    }

    public void setType(RoleType type) {
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

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(String createdUserId) {
        this.createdUserId = createdUserId;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedUserId() {
        return updatedUserId;
    }

    public void setUpdatedUserId(String updatedUserId) {
        this.updatedUserId = updatedUserId;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getDeletedUserId() {
        return deletedUserId;
    }

    public void setDeletedUserId(String deletedUserId) {
        this.deletedUserId = deletedUserId;
    }

    public LocalDateTime getDeletedTime() {
        return deletedTime;
    }

    public void setDeletedTime(LocalDateTime deletedTime) {
        this.deletedTime = deletedTime;
    }

    public List<RoleResponse> getChildren() {
        return children;
    }

    public void setChildren(List<RoleResponse> children) {
        this.children = children;
    }

    public List<Long> getWhitePermissionIds() {
        return whitePermissionIds;
    }

    public void setWhitePermissionIds(List<Long> whitePermissionIds) {
        this.whitePermissionIds = whitePermissionIds;
    }

    public List<PermissionResponse> getWhitePermissions() {
        return whitePermissions;
    }

    public void setWhitePermissions(List<PermissionResponse> whitePermissions) {
        this.whitePermissions = whitePermissions;
    }

    public List<Long> getBlackPermissionIds() {
        return blackPermissionIds;
    }

    public void setBlackPermissionIds(List<Long> blackPermissionIds) {
        this.blackPermissionIds = blackPermissionIds;
    }

    public List<PermissionResponse> getBlackPermissions() {
        return blackPermissions;
    }

    public void setBlackPermissions(List<PermissionResponse> blackPermissions) {
        this.blackPermissions = blackPermissions;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RoleResponse that = (RoleResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(code, that.code) && type == that.type && Objects.equals(sortValue, that.sortValue) && Objects.equals(parentId, that.parentId) && Objects.equals(remark, that.remark) && Objects.equals(deleted, that.deleted) && Objects.equals(createdUserId, that.createdUserId) && Objects.equals(createdTime, that.createdTime) && Objects.equals(updatedUserId, that.updatedUserId) && Objects.equals(updatedTime, that.updatedTime) && Objects.equals(deletedUserId, that.deletedUserId) && Objects.equals(deletedTime, that.deletedTime) && Objects.equals(children, that.children) && Objects.equals(whitePermissionIds, that.whitePermissionIds) && Objects.equals(whitePermissions, that.whitePermissions) && Objects.equals(blackPermissionIds, that.blackPermissionIds) && Objects.equals(blackPermissions, that.blackPermissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, type, sortValue, parentId, remark, deleted, createdUserId, createdTime, updatedUserId, updatedTime, deletedUserId, deletedTime, children, whitePermissionIds, whitePermissions, blackPermissionIds, blackPermissions);
    }

    @Override
    public String toString() {
        return "RoleResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", type=" + type +
                ", sortValue=" + sortValue +
                ", parentId=" + parentId +
                ", remark='" + remark + '\'' +
                ", deleted=" + deleted +
                ", createdUserId='" + createdUserId + '\'' +
                ", createdTime=" + createdTime +
                ", updatedUserId='" + updatedUserId + '\'' +
                ", updatedTime=" + updatedTime +
                ", deletedUserId='" + deletedUserId + '\'' +
                ", deletedTime=" + deletedTime +
                ", children=" + children +
                ", whitePermissionIds=" + whitePermissionIds +
                ", whitePermissions=" + whitePermissions +
                ", blackPermissionIds=" + blackPermissionIds +
                ", blackPermissions=" + blackPermissions +
                '}';
    }
}