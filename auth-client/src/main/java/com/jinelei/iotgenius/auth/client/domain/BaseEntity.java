package com.jinelei.iotgenius.auth.client.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.time.LocalDateTime;

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
    protected Long createdUserId;
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    protected LocalDateTime createdTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新人")
    protected Long updatedUserId;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    protected LocalDateTime updatedTime;

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

    public Long getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(Long createdUserId) {
        this.createdUserId = createdUserId;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public Long getUpdatedUserId() {
        return updatedUserId;
    }

    public void setUpdatedUserId(Long updatedUserId) {
        this.updatedUserId = updatedUserId;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((remark == null) ? 0 : remark.hashCode());
        result = prime * result + ((createdUserId == null) ? 0 : createdUserId.hashCode());
        result = prime * result + ((createdTime == null) ? 0 : createdTime.hashCode());
        result = prime * result + ((updatedUserId == null) ? 0 : updatedUserId.hashCode());
        result = prime * result + ((updatedTime == null) ? 0 : updatedTime.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BaseEntity other = (BaseEntity) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (remark == null) {
            if (other.remark != null)
                return false;
        } else if (!remark.equals(other.remark))
            return false;
        if (createdUserId == null) {
            if (other.createdUserId != null)
                return false;
        } else if (!createdUserId.equals(other.createdUserId))
            return false;
        if (createdTime == null) {
            if (other.createdTime != null)
                return false;
        } else if (!createdTime.equals(other.createdTime))
            return false;
        if (updatedUserId == null) {
            if (other.updatedUserId != null)
                return false;
        } else if (!updatedUserId.equals(other.updatedUserId))
            return false;
        if (updatedTime == null) {
            if (other.updatedTime != null)
                return false;
        } else if (!updatedTime.equals(other.updatedTime))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "BaseEntity [id=" + id + ", remark=" + remark + ", createdUserId=" + createdUserId + ", createdTime="
                + createdTime + ", updatedUserId=" + updatedUserId + ", updatedTime=" + updatedTime + "]";
    }

}
