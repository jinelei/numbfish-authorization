package com.jinelei.numbfish.authorization.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import com.jinelei.numbfish.authorization.enumeration.RoleType;

@SuppressWarnings("unused")
@Schema(description = "角色创建请求对象")
public class RoleCreateRequest implements Serializable {
    @NotBlank(message = "角色名称不能为空")
    @Schema(description = "角色名称")
    private String name;
    @NotBlank(message = "角色编码不能为空")
    @Schema(description = "角色编码")
    private String code;
    @NotNull(message = "角色类型不能为空")
    @Schema(description = "角色类型")
    private RoleType type;
    @Schema(description = "排序值")
    private Integer sortValue;
    @Schema(description = "上级角色编码")
    private Long parentId;
    @Schema(description = "角色备注")
    protected String remark;
    @NotNull(message = "白名单权限列表不能为空")
    @Schema(description = "白名单权限列表")
    protected List<Long> whitePermissionIds;
    @NotNull(message = "黑名单权限列表不能为空")
    @Schema(description = "黑名单权限列表")
    protected List<Long> blackPermissionIds;

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<Long> getWhitePermissionIds() {
        return whitePermissionIds;
    }

    public void setWhitePermissionIds(List<Long> whitePermissionIds) {
        this.whitePermissionIds = whitePermissionIds;
    }

    public List<Long> getBlackPermissionIds() {
        return blackPermissionIds;
    }

    public void setBlackPermissionIds(List<Long> blackPermissionIds) {
        this.blackPermissionIds = blackPermissionIds;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RoleCreateRequest that = (RoleCreateRequest) o;
        return Objects.equals(name, that.name) && Objects.equals(code, that.code) && type == that.type && Objects.equals(sortValue, that.sortValue) && Objects.equals(parentId, that.parentId) && Objects.equals(remark, that.remark) && Objects.equals(whitePermissionIds, that.whitePermissionIds) && Objects.equals(blackPermissionIds, that.blackPermissionIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, code, type, sortValue, parentId, remark, whitePermissionIds, blackPermissionIds);
    }

    @Override
    public String toString() {
        return "RoleCreateRequest{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", type=" + type +
                ", sortValue=" + sortValue +
                ", parentId=" + parentId +
                ", remark='" + remark + '\'' +
                ", whitePermissionIds=" + whitePermissionIds +
                ", blackPermissionIds=" + blackPermissionIds +
                '}';
    }
}
