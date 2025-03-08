package com.jinelei.iotgenius.auth.client.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@SuppressWarnings("rawtypes")
@Schema(description = "基础实体对象")
public class BaseEntity<T> implements Serializable {
    @TableId
    @Schema(description = "id")
    protected T id;
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "权限备注")
    protected String remark;
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建人")
    protected String createdUserId;
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    protected LocalDateTime createdTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新人")
    protected String updatedUserId;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    protected LocalDateTime updatedTime;
    @TableLogic
    @Schema(description = "是否启用")
    protected Boolean deleted;
    @Schema(description = "删除人")
    protected String deletedUserId;
    @Schema(description = "删除时间")
    protected LocalDateTime deletedTime;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(String createdUserId) {
        this.createdUserId = createdUserId;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedUserId() {
        return updatedUserId;
    }

    public void setUpdatedUserId(String updatedUserId) {
        this.updatedUserId = updatedUserId;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getDeletedUserId() {
        return deletedUserId;
    }

    public void setDeletedUserId(String deletedUserId) {
        this.deletedUserId = deletedUserId;
    }

    public LocalDateTime getDeletedTime() {
        return deletedTime;
    }

    public void setDeletedTime(LocalDateTime deletedTime) {
        this.deletedTime = deletedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity<?> that = (BaseEntity<?>) o;
        return Objects.equals(id, that.id) && Objects.equals(remark, that.remark) && Objects.equals(createdUserId, that.createdUserId) && Objects.equals(createdTime, that.createdTime) && Objects.equals(updatedUserId, that.updatedUserId) && Objects.equals(updatedTime, that.updatedTime) && Objects.equals(deleted, that.deleted) && Objects.equals(deletedUserId, that.deletedUserId) && Objects.equals(deletedTime, that.deletedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, remark, createdUserId, createdTime, updatedUserId, updatedTime, deleted, deletedUserId, deletedTime);
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + id +
                ", remark='" + remark + '\'' +
                ", createdUserId='" + createdUserId + '\'' +
                ", createdTime=" + createdTime +
                ", updatedUserId='" + updatedUserId + '\'' +
                ", updatedTime=" + updatedTime +
                ", deleted=" + deleted +
                ", deletedUserId='" + deletedUserId + '\'' +
                ", deletedTime=" + deletedTime +
                '}';
    }
}
