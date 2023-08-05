package com.github.desperado2.data.open.api.api.manage.model;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 开发API模型
 * @author tu nan
 * @date 2023/2/9
 **/

public class OpenApiScriptsReleaseList {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("API的ID")
    private Long apiId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
