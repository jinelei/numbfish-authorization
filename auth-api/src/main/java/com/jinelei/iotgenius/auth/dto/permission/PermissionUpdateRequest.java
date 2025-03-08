package com.jinelei.iotgenius.auth.dto.permission;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.Objects;

@Schema(description = "权限修改请求对象")
public class PermissionUpdateRequest implements Serializable {
    @NotBlank(message = "权限id不能为空")
    @Schema(description = "id")
    protected Long id;
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
        PermissionUpdateRequest that = (PermissionUpdateRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(code, that.code) && Objects.equals(sortValue, that.sortValue) && Objects.equals(parentId, that.parentId) && Objects.equals(remark, that.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, sortValue, parentId, remark);
    }

    @Override
    public String toString() {
        return "PermissionUpdateRequest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", sortValue=" + sortValue +
                ", parentId=" + parentId +
                ", remark='" + remark + '\'' +
                '}';
    }

}
