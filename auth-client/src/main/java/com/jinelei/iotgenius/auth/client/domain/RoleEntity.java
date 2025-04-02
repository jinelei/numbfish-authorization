package com.jinelei.iotgenius.auth.client.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jinelei.iotgenius.auth.enumeration.RoleType;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("all")
@TableName("role")
public class RoleEntity extends BaseEntity<Long> {
    protected String name;
    protected String code;
    protected RoleType type;
    protected Integer sortValue;
    protected Long parentId;
    @TableField(exist = false)
    private List<RoleEntity> children;
    @TableField(exist = false)
    private List<PermissionEntity> whitePermissions;
    @TableField(exist = false)
    private List<PermissionEntity> blackPermissions;

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

    public List<RoleEntity> getChildren() {
        return children;
    }

    public void setChildren(List<RoleEntity> children) {
        this.children = children;
    }

    public List<PermissionEntity> getWhitePermissions() {
        return whitePermissions;
    }

    public void setWhitePermissions(List<PermissionEntity> whitePermissions) {
        this.whitePermissions = whitePermissions;
    }

    public List<PermissionEntity> getBlackPermissions() {
        return blackPermissions;
    }

    public void setBlackPermissions(List<PermissionEntity> blackPermissions) {
        this.blackPermissions = blackPermissions;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RoleEntity that = (RoleEntity) o;
        return Objects.equals(name, that.name) && Objects.equals(code, that.code) && type == that.type && Objects.equals(sortValue, that.sortValue) && Objects.equals(parentId, that.parentId) && Objects.equals(children, that.children) && Objects.equals(whitePermissions, that.whitePermissions) && Objects.equals(blackPermissions, that.blackPermissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, code, type, sortValue, parentId, children, whitePermissions, blackPermissions);
    }

    @Override
    public String toString() {
        return "RoleEntity [name=" + name + ", code=" + code + ", type=" + type + ", sortValue=" + sortValue + ", id="
                + id + ", parentId=" + parentId + ", children=" + children + ", remark=" + remark
                + ", whitePermissions=" + whitePermissions + ", createdUserId=" + createdUserId + ", blackPermissions="
                + blackPermissions + ", createdTime=" + createdTime + ", updatedUserId=" + updatedUserId
                + ", updatedTime=" + updatedTime + "]";
    }
}
