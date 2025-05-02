package com.jinelei.numbfish.authorization.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.List;

import com.jinelei.numbfish.authorization.enumeration.PermissionType;
import com.jinelei.numbfish.authorization.enumeration.TreeBuildMode;

@SuppressWarnings("unused")
@Schema(description = "权限查询请求对象")
public class PermissionQueryRequest implements Serializable {
    @Schema(description = "id")
    protected Long id;
    @Schema(description = "id列表")
    protected List<Long> ids;
    @Schema(description = "权限名称")
    private String name;
    @Schema(description = "权限编码")
    private String code;
    @Schema(description = "权限类型")
    private PermissionType type;
    @Schema(description = "排序值")
    private Integer sortValue;
    @Schema(description = "上级权限编码")
    private String parentId;
    @Schema(description = "权限备注")
    protected String remark;
    @Schema(description = "树构建模式")
    protected TreeBuildMode mode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PermissionQueryRequest other = (PermissionQueryRequest) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (ids == null) {
            if (other.ids != null)
                return false;
        } else if (!ids.equals(other.ids))
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
        if (mode != other.mode) {
            return false;
        }
        return true;
    }

    public TreeBuildMode getMode() {
        return mode;
    }

    public void setMode(TreeBuildMode mode) {
        this.mode = mode;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((ids == null) ? 0 : ids.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((sortValue == null) ? 0 : sortValue.hashCode());
        result = prime * result + ((parentId == null) ? 0 : parentId.hashCode());
        result = prime * result + ((remark == null) ? 0 : remark.hashCode());
        result = prime * result + ((mode == null) ? 0 : mode.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "PermissionQueryRequest [id=" + id + ", ids=" + ids + ", name=" + name + ", code=" + code + ", type="
                + type + ", sortValue=" + sortValue + ", parentId=" + parentId + ", remark=" + remark + ", mode=" + mode
                + "]";
    }

}