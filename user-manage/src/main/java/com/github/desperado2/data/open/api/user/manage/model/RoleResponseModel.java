package com.github.desperado2.data.open.api.user.manage.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 角色
 * @author tu nan
 * @date 2023/2/10
 **/
@ApiModel(value = "RoleResponseModel", description = "角色")
public class RoleResponseModel {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("角色名称")
    private String name;

    @ApiModelProperty("默认角色")
    private Integer defaultRole;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDefaultRole() {
        return defaultRole;
    }

    public void setDefaultRole(Integer defaultRole) {
        this.defaultRole = defaultRole;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
