package com.jinelei.iotgenius.auth.client.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jinelei.iotgenius.auth.enumeration.PermissionType;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("all")
@TableName("permission")
public class PermissionEntity extends BaseEntity<Long> {
    protected String name;
    protected String code;
    protected PermissionType type;
    protected Integer sortValue;
    protected Long parentId;
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
        return Objects.equals(name, that.name) && Objects.equals(code, that.code) && type == that.type && Objects.equals(sortValue, that.sortValue) && Objects.equals(parentId, that.parentId) && Objects.equals(children, that.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, code, type, sortValue, parentId, children);
    }

    @Override
    public String toString() {
        return "PermissionEntity [name=" + name + ", code=" + code + ", type=" + type + ", id=" + id + ", sortValue="
                + sortValue + ", parentId=" + parentId + ", remark=" + remark + ", children=" + children
                + ", createdUserId=" + createdUserId + ", createdTime=" + createdTime + ", updatedUserId="
                + updatedUserId + ", updatedTime=" + updatedTime + "]";
    }
}
