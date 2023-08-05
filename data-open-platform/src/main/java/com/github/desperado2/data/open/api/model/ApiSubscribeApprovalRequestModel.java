package com.github.desperado2.data.open.api.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * 接口订阅审核模型
 * @author tu nan
 * @date 2023/2/15
 **/
@ApiModel(value = "ApiSubscribeApprovalRequestModel", description = "订阅数据审核模型")
public class ApiSubscribeApprovalRequestModel {

    @ApiModelProperty("订阅id")
    @NotBlank(message = "subscribeId不能为空")
    private Long subscribeId;

    @ApiModelProperty("通过状态，1-通过，2-不通过")
    @NotBlank(message = "status不能为空")
    private Integer status;

    @ApiModelProperty("不通过原因")
    private String refuseMessage;

    public Long getSubscribeId() {
        return subscribeId;
    }

    public void setSubscribeId(Long subscribeId) {
        this.subscribeId = subscribeId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRefuseMessage() {
        return refuseMessage;
    }

    public void setRefuseMessage(String refuseMessage) {
        this.refuseMessage = refuseMessage;
    }
}
