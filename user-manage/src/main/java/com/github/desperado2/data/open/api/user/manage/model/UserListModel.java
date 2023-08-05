package com.github.desperado2.data.open.api.user.manage.model;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 用户信息
 * @author tu nan
 * @date 2023/2/9
 **/
public class UserListModel {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("角色Id")
    private Long roleId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
