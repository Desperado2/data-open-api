package com.github.desperado2.data.open.api.log.manage.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * api日志
 * @author tu nan
 * @date 2023/2/9
 **/
@ApiModel(value = "ApiLogsModel", description = "API日志表")
public class ApiLogsModel {

    @ApiModelProperty("唯一主键")
    private Long id;

    @ApiModelProperty("日志唯一KEY")
    private String logKey;

    @ApiModelProperty("APPKEY")
    private String appKey;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户名称")
    private String username;

    @ApiModelProperty("请求的APIId")
    private Long apiId;

    @ApiModelProperty("请求的API名称")
    private String apiName;

    @ApiModelProperty("请求时间戳")
    private Long requestTime;

    @ApiModelProperty("响应时间戳")
    private Long responseTime;

    @ApiModelProperty("请求环境")
    private String requestEnvironment;

    @ApiModelProperty("请求参数")
    private String requestParams;

    @ApiModelProperty("返回结果数量")
    private Integer resultCount;

    @ApiModelProperty("请求状态")
    private Integer status;

    /**
     * 日志key
     */
    private String requestUrl;

    @ApiModelProperty("错误信息")
    private String errorMsg;

    @ApiModelProperty("创建时间")
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogKey() {
        return logKey;
    }

    public void setLogKey(String logKey) {
        this.logKey = logKey;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
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

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    public Long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Long requestTime) {
        this.requestTime = requestTime;
    }

    public Long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Long responseTime) {
        this.responseTime = responseTime;
    }

    public String getRequestEnvironment() {
        return requestEnvironment;
    }

    public void setRequestEnvironment(String requestEnvironment) {
        this.requestEnvironment = requestEnvironment;
    }

    public String getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }

    public Integer getResultCount() {
        return resultCount;
    }

    public void setResultCount(Integer resultCount) {
        this.resultCount = resultCount;
    }

    public Integer getStatus() {
        return status;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
