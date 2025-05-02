package com.jinelei.numbfish.authorization.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jinelei.numbfish.authorization.enumeration.RoleType;
import com.jinelei.numbfish.common.entity.BaseEntity;

import java.util.List;

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
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        RoleEntity other = (RoleEntity) obj;
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
        if (level == null) {
            if (other.level != null)
                return false;
        } else if (!level.equals(other.level))
            return false;
        if (ancestor == null) {
            if (other.ancestor != null)
                return false;
        } else if (!ancestor.equals(other.ancestor))
            return false;
        if (children == null) {
            if (other.children != null)
                return false;
        } else if (!children.equals(other.children))
            return false;
        if (whitePermissions == null) {
            if (other.whitePermissions != null)
                return false;
        } else if (!whitePermissions.equals(other.whitePermissions))
            return false;
        if (blackPermissions == null) {
            return other.blackPermissions == null;
        } else {
            return blackPermissions.equals(other.blackPermissions);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((sortValue == null) ? 0 : sortValue.hashCode());
        result = prime * result + ((parentId == null) ? 0 : parentId.hashCode());
        result = prime * result + ((level == null) ? 0 : level.hashCode());
        result = prime * result + ((ancestor == null) ? 0 : ancestor.hashCode());
        result = prime * result + ((children == null) ? 0 : children.hashCode());
        result = prime * result + ((whitePermissions == null) ? 0 : whitePermissions.hashCode());
        result = prime * result + ((blackPermissions == null) ? 0 : blackPermissions.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "RoleEntity [name=" + name + ", code=" + code + ", type=" + type + ", sortValue=" + sortValue
                + ", parentId=" + parentId + ", level=" + level + ", ancestor=" + ancestor + ", children=" + children
                + ", whitePermissions=" + whitePermissions + ", blackPermissions=" + blackPermissions + "]";
    }
}
