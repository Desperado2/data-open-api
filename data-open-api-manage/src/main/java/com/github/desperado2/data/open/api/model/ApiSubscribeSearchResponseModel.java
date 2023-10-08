package com.github.desperado2.data.open.api.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 接口订阅审核模型
 * @author tu nan
 * @date 2023/2/15
 **/
@ApiModel(value = "ApiSubscribeSearchResponseModel", description = "订阅数据列表返回模型")
public class ApiSubscribeSearchResponseModel{

    @ApiModelProperty("ApiId")
    private Long apiId;

    @ApiModelProperty("订阅id")
    private Long subscribeId;

    @ApiModelProperty("API名称")
    private String apiName;

    @ApiModelProperty("订阅者id")
    private Long userId;

    @ApiModelProperty("订阅者名称")
    private String username;

    @ApiModelProperty("订阅状态")
    private Integer status;

    @ApiModelProperty("API状态")
    private Integer apiStatus;

    @ApiModelProperty("申请时间")
    private Date createTime;

    @ApiModelProperty("拒绝原因")
    private String refuseMessage;

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    public Long getSubscribeId() {
        return subscribeId;
    }

    public void setSubscribeId(Long subscribeId) {
        this.subscribeId = subscribeId;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getApiStatus() {
        return apiStatus;
    }

    public void setApiStatus(Integer apiStatus) {
        this.apiStatus = apiStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRefuseMessage() {
        return refuseMessage;
    }

    public void setRefuseMessage(String refuseMessage) {
        this.refuseMessage = refuseMessage;
    }
}
