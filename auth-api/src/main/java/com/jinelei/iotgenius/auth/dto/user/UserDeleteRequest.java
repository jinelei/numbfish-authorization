package com.jinelei.iotgenius.auth.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Schema(description = "用户删除请求对象")
public class UserDeleteRequest implements Serializable {
    @Schema(description = "id")
    protected Long id;
    @Schema(description = "id列表")
    protected List<Long> ids;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserDeleteRequest that = (UserDeleteRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(ids, that.ids);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ids);
    }

    @Override
    public String toString() {
        return "UserDeleteRequest{" +
                "id=" + id +
                ", ids=" + ids +
                '}';
    }
}
