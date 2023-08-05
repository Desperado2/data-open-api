package com.github.desperado2.data.open.api.user.manage.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * 角色
 * @author tu nan
 * @date 2023/2/10
 **/
@ApiModel(value = "RoleRequestModel", description = "角色请求模型")
public class RoleRequestModel {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("编码")
    @NotBlank(message = "编码不能为空")
    private String code;

    @ApiModelProperty("角色名称")
    @NotBlank(message = "名称不能为空")
    private String name;

    @ApiModelProperty("默认角色")
    @NotBlank(message = "角色不能为空")
    private Integer defaultRole;

    @ApiModelProperty("状态")
    @NotBlank(message = "状态不能为空")
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
}
