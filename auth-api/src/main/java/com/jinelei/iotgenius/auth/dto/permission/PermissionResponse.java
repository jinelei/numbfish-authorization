package com.jinelei.iotgenius.auth.dto.permission;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("权限响应对象")
public class PermissionResponse {
    @ApiModelProperty("权限id")
    private Long permissionId;
    @ApiModelProperty("权限名称")
    private String name;
    @ApiModelProperty("权限编码")
    private String code;
    @ApiModelProperty("排序值")
    private Integer sortValue;
    @ApiModelProperty("是否启用")
    private Boolean enabled;
    @ApiModelProperty("上级权限编码")
    private String ParentCode;
    @ApiModelProperty("权限备注")
    private String remark;
    @ApiModelProperty("创建人")
    private String creator;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("更新人")
    private String updater;
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
    @ApiModelProperty("删除人")
    private String deleter;
    @ApiModelProperty("删除时间")
    private LocalDateTime deleteTime;

    // Getters and Setters
    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getParentCode() {
        return ParentCode;
    }

    public void setParentCode(String ParentCode) {
        this.ParentCode = ParentCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getDeleter() {
        return deleter;
    }

    public void setDeleter(String deleter) {
        this.deleter = deleter;
    }

    public LocalDateTime getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(LocalDateTime deleteTime) {
        this.deleteTime = deleteTime;
    }
    
    @Override
    public String toString() {
        return "PermissionResponse{" +
                "permissionId=" + permissionId +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", sortValue=" + sortValue +
                ", enabled=" + enabled +
                ", ParentCode='" + ParentCode + '\'' +
                ", remark='" + remark + '\'' +
                ", creator='" + creator + '\'' +
                ", createTime=" + createTime +
                ", updater='" + updater + '\'' +
                ", updateTime=" + updateTime +
                ", deleter='" + deleter + '\'' +
                ", deleteTime=" + deleteTime +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(permissionId, name, code, sortValue, enabled, ParentCode, remark, creator, createTime, updater, updateTime, deleter, deleteTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PermissionResponse that = (PermissionResponse) o;
        return Objects.equals(permissionId, that.permissionId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(code, that.code) &&
                Objects.equals(sortValue, that.sortValue) &&
                Objects.equals(enabled, that.enabled) &&
                Objects.equals(ParentCode, that.ParentCode) &&
                Objects.equals(remark, that.remark) &&
                Objects.equals(creator, that.creator) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(updater, that.updater) &&
                Objects.equals(updateTime, that.updateTime) &&
                Objects.equals(deleter, that.deleter) &&
                Objects.equals(deleteTime, that.deleteTime);
    }
}
