package com.jinelei.numbfish.authorization.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jinelei.numbfish.authorization.enumeration.PermissionType;
import com.jinelei.numbfish.common.entity.BaseEntity;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
@TableName("permission")
public class PermissionEntity extends BaseEntity<Long> {
    protected String name;
    protected String code;
    protected PermissionType type;
    protected Integer sortValue;
    protected Long parentId;
    protected String path;
    protected String icon;
    @TableField(exist = false)
    private List<PermissionEntity> children;

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<PermissionEntity> getChildren() {
        return children;
    }

    public void setChildren(List<PermissionEntity> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PermissionEntity that = (PermissionEntity) o;
        return Objects.equals(name, that.name) && Objects.equals(code, that.code) && type == that.type && Objects.equals(sortValue, that.sortValue) && Objects.equals(parentId, that.parentId) && Objects.equals(path, that.path) && Objects.equals(icon, that.icon) && Objects.equals(children, that.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, code, type, sortValue, parentId, path, icon, children);
    }

    @Override
    public String toString() {
        return "PermissionEntity{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", type=" + type +
                ", sortValue=" + sortValue +
                ", parentId=" + parentId +
                ", path='" + path + '\'' +
                ", icon='" + icon + '\'' +
                ", children=" + children +
                ", id=" + id +
                ", remark='" + remark + '\'' +
                ", createdUserId=" + createdUserId +
                ", createdTime=" + createdTime +
                ", updatedUserId=" + updatedUserId +
                ", updatedTime=" + updatedTime +
                '}';
    }
}
