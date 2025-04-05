package com.jinelei.iotgenius.auth.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
@Schema(description = "用户查询请求对象")
public class UserQueryRequest implements Serializable {
    @Schema(description = "id")
    protected Long id;
    @Schema(description = "id列表")
    protected List<Long> ids;
    @Schema(description = "用户名称")
    private String username;
    @Schema(description = "邮箱")
    private String email;
    @Schema(description = "手机号")
    private String phone;
    @Schema(description = "用户备注")
    protected String remark;
    @Schema(description = "角色列表")
    protected List<Long> roleIds;

    public Long getId() {
        return id;
    }

    public List<Long> getIds() {
        return ids;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getRemark() {
        return remark;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserQueryRequest that = (UserQueryRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(ids, that.ids) && Objects.equals(username, that.username) && Objects.equals(email, that.email) && Objects.equals(phone, that.phone) && Objects.equals(remark, that.remark) && Objects.equals(roleIds, that.roleIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ids, username, email, phone, remark, roleIds);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }
}