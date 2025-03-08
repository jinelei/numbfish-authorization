package com.jinelei.iotgenius.auth.dto.permission;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Schema(description = "权限删除请求对象")
public class PermissionDeleteRequest implements Serializable {
    @Schema(description = "id")
    protected Long id;
    @Schema(description = "id列表")
    protected List<Long> ids;
    @Schema(description = "上级权限编码")
    private Long parentId;

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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PermissionDeleteRequest that = (PermissionDeleteRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(ids, that.ids) && Objects.equals(parentId, that.parentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ids, parentId);
    }

    @Override
    public String toString() {
        return "PermissionDeleteRequest{" +
                "id=" + id +
                ", ids=" + ids +
                ", parentId=" + parentId +
                '}';
    }
}
