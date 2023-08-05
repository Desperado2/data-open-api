package com.github.desperado2.data.open.api.cache.manage.model;


import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * api日志
 * @author tu nan
 * @date 2023/2/9
 **/

public class ApiLogsCache {

    private Long id;

    @ApiModelProperty("日志唯一key")
    private String logKey;

    @ApiModelProperty("APPKEY")
    private String appKey;

    @ApiModelProperty("请求环境")
    private String requestEnvironment;

    @ApiModelProperty("请求的APIid")
    private Long apiId;

    @ApiModelProperty("请求的API名称")
    private String apiName;

    @ApiModelProperty("用户的id")
    private Long userId;

    @ApiModelProperty("用户的名称")
    private String username;

    @ApiModelProperty("请求时间戳")
    private Long requestTime;

    @ApiModelProperty("响应时间戳")
    private Long responseTime;

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

    private ApiLogsCache(Builder builder) {
        setId(builder.id);
        setLogKey(builder.logKey);
        setAppKey(builder.appKey);
        setRequestEnvironment(builder.requestEnvironment);
        setApiId(builder.apiId);
        setApiName(builder.apiName);
        setUserId(builder.userId);
        setUsername(builder.username);
        setRequestTime(builder.requestTime);
        setResponseTime(builder.responseTime);
        setRequestParams(builder.requestParams);
        setResultCount(builder.resultCount);
        setStatus(builder.status);
        setRequestUrl(builder.requestUrl);
        setErrorMsg(builder.errorMsg);
        setCreateTime(builder.createTime);
    }

    public ApiLogsCache() {
    }

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

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }
    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
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

    public String getRequestEnvironment() {
        return requestEnvironment;
    }

    public void setRequestEnvironment(String requestEnvironment) {
        this.requestEnvironment = requestEnvironment;
    }

    public static final class Builder {
        private Long id;
        private String logKey;
        private String appKey;
        private String requestEnvironment;
        private Long apiId;
        private String apiName;
        private Long userId;
        private String username;
        private Long requestTime;
        private Long responseTime;
        private String requestParams;
        private Integer resultCount;
        private Integer status;
        private String requestUrl;
        private String errorMsg;
        private Date createTime;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(Long val) {
            id = val;
            return this;
        }

        public Builder logKey(String val) {
            logKey = val;
            return this;
        }

        public Builder appKey(String val) {
            appKey = val;
            return this;
        }

        public Builder requestEnvironment(String val) {
            requestEnvironment = val;
            return this;
        }

        public Builder apiId(Long val) {
            apiId = val;
            return this;
        }

        public Builder apiName(String val) {
            apiName = val;
            return this;
        }

        public Builder userId(Long val) {
            userId = val;
            return this;
        }

        public Builder username(String val) {
            username = val;
            return this;
        }

        public Builder requestTime(Long val) {
            requestTime = val;
            return this;
        }

        public Builder responseTime(Long val) {
            responseTime = val;
            return this;
        }

        public Builder requestParams(String val) {
            requestParams = val;
            return this;
        }

        public Builder resultCount(Integer val) {
            resultCount = val;
            return this;
        }

        public Builder status(Integer val) {
            status = val;
            return this;
        }

        public Builder requestUrl(String val) {
            requestUrl = val;
            return this;
        }

        public Builder errorMsg(String val) {
            errorMsg = val;
            return this;
        }

        public Builder createTime(Date val) {
            createTime = val;
            return this;
        }

        public ApiLogsCache build() {
            return new ApiLogsCache(this);
        }
    }
}
