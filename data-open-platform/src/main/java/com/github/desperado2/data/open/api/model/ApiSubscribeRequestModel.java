package com.github.desperado2.data.open.api.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * 接口订阅数据库模型
 * @author tu nan
 * @date 2023/2/13
 **/
@ApiModel(value = "ApiSubscribeRequestModel", description = "订阅数据模型")
public class ApiSubscribeRequestModel {

    @ApiModelProperty("apiId")
    @NotNull(message = "id不能为空")
    private Long apiId;

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }
}
