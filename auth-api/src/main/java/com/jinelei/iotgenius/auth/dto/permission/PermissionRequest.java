package com.jinelei.iotgenius.auth.dto.permission;

import com.jinelei.iotgenius.common.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@ApiModel("权限请求对象")
public class PermissionRequest extends BaseEntity<Long> {
    @NotBlank(groups = { Create.class, Update.class }, message = "权限名称不能为空")
    @ApiModelProperty("权限名称")
    private String name;
    @NotBlank(groups = { Create.class, Update.class }, message = "权限编码不能为空")
    @ApiModelProperty("权限编码")
    private String code;
    @ApiModelProperty("排序值")
    private Integer sortValue;
    @ApiModelProperty("上级权限编码")
    private String parentId;

    @Override
    @NotNull(groups = { Update.class, Delete.class }, message = "权限的ID不能为空")
    public Long getId() {
        return id;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((sortValue == null) ? 0 : sortValue.hashCode());
        result = prime * result + ((parentId == null) ? 0 : parentId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        PermissionRequest other = (PermissionRequest) obj;
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
        return true;
    }

    @Override
    public String toString() {
        return "PermissionRequest [name=" + name + ", code=" + code + ", sortValue=" + sortValue + ", parentId="
                + parentId + "]";
    }

    public interface Create {
    }

    public interface Update {
    }

    public interface Delete {
    }
}