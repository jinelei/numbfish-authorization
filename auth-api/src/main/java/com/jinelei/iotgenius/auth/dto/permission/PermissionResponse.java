package com.jinelei.iotgenius.auth.dto.permission;

import com.jinelei.iotgenius.common.response.BaseResponse;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("权限响应对象")
public class PermissionResponse extends BaseResponse<Long> {
    @ApiModelProperty("权限名称")
    private String name;
    @ApiModelProperty("权限编码")
    private String code;
    @ApiModelProperty("排序值")
    private Integer sortValue;
    @ApiModelProperty("上级权限编码")
    private String parentCode;

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

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((sortValue == null) ? 0 : sortValue.hashCode());
        result = prime * result + ((parentCode == null) ? 0 : parentCode.hashCode());
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
        PermissionResponse other = (PermissionResponse) obj;
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
        if (parentCode == null) {
            if (other.parentCode != null)
                return false;
        } else if (!parentCode.equals(other.parentCode))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PermissionResponse [name=" + name + ", code=" + code + ", sortValue=" + sortValue + ", parentCode="
                + parentCode + "]";
    }

}
