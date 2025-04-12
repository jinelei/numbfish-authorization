package com.jinelei.numbfish.auth.dto.client;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
@Schema(description = "客户端删除请求对象")
public class ClientDeleteRequest implements Serializable {
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
        ClientDeleteRequest that = (ClientDeleteRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(ids, that.ids);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ids);
    }

    @Override
    public String toString() {
        return "ClientDeleteRequest{" +
                "id=" + id +
                ", ids=" + ids +
                '}';
    }
}
