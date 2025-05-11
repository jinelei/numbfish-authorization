package com.jinelei.numbfish.authorization.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jinelei.numbfish.authorization.enumeration.RoleType;
import com.jinelei.numbfish.common.entity.BaseEntity;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
@TableName("role")
public class RoleEntity extends BaseEntity<Long> {
    protected String name;
    protected String code;
    protected RoleType type;
    protected Integer sortValue;
    protected Long parentId;
    @TableField(exist = false)
    private Integer level;
    @TableField(exist = false)
    private String ancestor;
    @TableField(exist = false)
    private List<RoleEntity> children;
    @TableField(exist = false)
    private List<Long> permissionIds;
    @TableField(exist = false)
    private List<PermissionEntity> permissions;

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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getAncestor() {
        return ancestor;
    }

    public void setAncestor(String ancestor) {
        this.ancestor = ancestor;
    }

    public List<RoleEntity> getChildren() {
        return children;
    }

    public void setChildren(List<RoleEntity> children) {
        this.children = children;
    }

    public List<Long> getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(List<Long> permissionIds) {
        this.permissionIds = permissionIds;
    }

    public List<PermissionEntity> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionEntity> permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RoleEntity that = (RoleEntity) o;
        return Objects.equals(name, that.name) && Objects.equals(code, that.code) && type == that.type && Objects.equals(sortValue, that.sortValue) && Objects.equals(parentId, that.parentId) && Objects.equals(level, that.level) && Objects.equals(ancestor, that.ancestor) && Objects.equals(children, that.children) && Objects.equals(permissionIds, that.permissionIds) && Objects.equals(permissions, that.permissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, code, type, sortValue, parentId, level, ancestor, children, permissionIds, permissions);
    }

    @Override
    public String toString() {
        return "RoleEntity{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", type=" + type +
                ", sortValue=" + sortValue +
                ", parentId=" + parentId +
                ", level=" + level +
                ", ancestor='" + ancestor + '\'' +
                ", children=" + children +
                ", permissionIds=" + permissionIds +
                ", permissions=" + permissions +
                ", id=" + id +
                ", remark='" + remark + '\'' +
                ", createdUserId=" + createdUserId +
                ", createdTime=" + createdTime +
                ", updatedUserId=" + updatedUserId +
                ", updatedTime=" + updatedTime +
                '}';
    }
}
