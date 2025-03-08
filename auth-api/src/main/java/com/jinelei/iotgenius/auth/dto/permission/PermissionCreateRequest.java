package com.jinelei.iotgenius.auth.dto.permission;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.Objects;

@Schema(description = "权限创建请求对象")
public class PermissionCreateRequest implements Serializable {
    @NotBlank(message = "权限名称不能为空")
    @Schema(description = "权限名称")
    private String name;
    @NotBlank(message = "权限编码不能为空")
    @Schema(description = "权限编码")
    private String code;
    @Schema(description = "排序值")
    private Integer sortValue;
    @Schema(description = "上级权限编码")
    private Long parentId;
    @Schema(description = "权限备注")
    protected String remark;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PermissionCreateRequest that = (PermissionCreateRequest) o;
        return Objects.equals(name, that.name) && Objects.equals(code, that.code) && Objects.equals(sortValue, that.sortValue) && Objects.equals(parentId, that.parentId) && Objects.equals(remark, that.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, code, sortValue, parentId, remark);
    }

    @Override
    public String toString() {
        return "PermissionCreateRequest{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", sortValue=" + sortValue +
                ", parentId=" + parentId +
                ", remark='" + remark + '\'' +
                '}';
    }
}
