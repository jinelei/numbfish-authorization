package com.jinelei.numbfish.authorization.dto;

import com.jinelei.numbfish.authorization.enumeration.RoleType;
import com.jinelei.numbfish.common.response.TreeResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
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
    @Schema(description = "创建人用户 ID")
    private Long createdUserId;
    @Schema(description = "创建时间")
    private LocalDateTime createdTime;
    @Schema(description = "更新人用户 ID")
    private Long updatedUserId;
    @Schema(description = "更新时间")
    private LocalDateTime updatedTime;
    private List<RoleResponse> children;
    @Schema(description = "权限列表")
    protected List<Long> permissionIds;
    @Schema(description = "权限实体列表")
    protected List<PermissionResponse> permissions;


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

    public List<RoleResponse> getChildren() {
        return children;
    }

    public void setChildren(List<RoleResponse> children) {
        this.children = children;
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
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RoleResponse other = (RoleResponse) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        if (type != other.type)
            return false;
        if (sortValue == null) {
            if (other.sortValue != null)
                return false;
        } else if (!sortValue.equals(other.sortValue))
            return false;
        if (parentId == null) {
            if (other.parentId != null)
                return false;
        } else if (!parentId.equals(other.parentId))
            return false;
        if (remark == null) {
            if (other.remark != null)
                return false;
        } else if (!remark.equals(other.remark))
            return false;
        if (createdUserId == null) {
            if (other.createdUserId != null)
                return false;
        } else if (!createdUserId.equals(other.createdUserId))
            return false;
        if (createdTime == null) {
            if (other.createdTime != null)
                return false;
        } else if (!createdTime.equals(other.createdTime))
            return false;
        if (updatedUserId == null) {
            if (other.updatedUserId != null)
                return false;
        } else if (!updatedUserId.equals(other.updatedUserId))
            return false;
        if (updatedTime == null) {
            if (other.updatedTime != null)
                return false;
        } else if (!updatedTime.equals(other.updatedTime))
            return false;
        if (children == null) {
            if (other.children != null)
                return false;
        } else if (!children.equals(other.children))
            return false;
        if (permissionIds == null) {
            if (other.permissionIds != null)
                return false;
        } else if (!permissionIds.equals(other.permissionIds))
            return false;
        if (permissions == null) {
            if (other.permissions != null)
                return false;
        } else if (!permissions.equals(other.permissions))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((sortValue == null) ? 0 : sortValue.hashCode());
        result = prime * result + ((parentId == null) ? 0 : parentId.hashCode());
        result = prime * result + ((remark == null) ? 0 : remark.hashCode());
        result = prime * result + ((createdUserId == null) ? 0 : createdUserId.hashCode());
        result = prime * result + ((createdTime == null) ? 0 : createdTime.hashCode());
        result = prime * result + ((updatedUserId == null) ? 0 : updatedUserId.hashCode());
        result = prime * result + ((updatedTime == null) ? 0 : updatedTime.hashCode());
        result = prime * result + ((children == null) ? 0 : children.hashCode());
        result = prime * result + ((permissionIds == null) ? 0 : permissionIds.hashCode());
        result = prime * result + ((permissions == null) ? 0 : permissions.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "RoleResponse [id=" + id + ", name=" + name + ", code=" + code + ", type=" + type + ", sortValue="
                + sortValue + ", parentId=" + parentId + ", remark=" + remark + ", createdUserId=" + createdUserId
                + ", createdTime=" + createdTime + ", updatedUserId=" + updatedUserId + ", updatedTime=" + updatedTime
                + ", children=" + children + ", permissionIds=" + permissionIds + ", permissions=" + permissions + "]";
    }
}
