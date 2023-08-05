package com.github.desperado2.data.open.api.log.manage.model;


import com.alibaba.fastjson.JSONObject;
import com.github.desperado2.data.open.api.log.manage.enums.LogEventEnums;

/**
 * 日志事件模型
 * @author tu nan
 * @date 2023/2/13
 **/
public class LogEventModel {

    /**
     * 请求体
     */
    private String body;

    /**
     * 请求参数
     */
    private JSONObject params;

    /**
     * 请求环境
     */
    private String requestEnvironment;

    /**
     * 请求地址
     */
    private String address;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 事件类型
     */
    private LogEventEnums logEventEnums;

    /**
     * 日志key
     */
    private String logKey;

    /**
     * 日志key
     */
    private String requestUrl;

    /**
     * 响应体
     */
    private String responseBody;


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getRequestEnvironment() {
        return requestEnvironment;
    }

    public void setRequestEnvironment(String requestEnvironment) {
        this.requestEnvironment = requestEnvironment;
    }

    public JSONObject getParams() {
        return params;
    }

    public void setParams(JSONObject params) {
        this.params = params;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public LogEventEnums getLogEventEnums() {
        return logEventEnums;
    }

    public void setLogEventEnums(LogEventEnums logEventEnums) {
        this.logEventEnums = logEventEnums;
    }

    public String getLogKey() {
        return logKey;
    }

    public void setLogKey(String logKey) {
        this.logKey = logKey;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }
}
