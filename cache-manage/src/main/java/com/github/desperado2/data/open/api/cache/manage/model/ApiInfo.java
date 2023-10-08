package com.github.desperado2.data.open.api.cache.manage.model;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * API模型
 */
public class ApiInfo {

    private Long id;

    private String apiName;
    /**
     * 路径
     */

    private String path;

    /**
     * 方法支持列表
     */

    private String method;
    /**
     * API选项
     */

    private String options;
    /**
     * API模式，CODE/QL，分别为代码模式，或QL模式
     */
    private String type;
    /**
     * 注释说明
     */

    private String name;
    /**
     * 数据源
     */

    private String testDatasource;

    /**
     * 数据源
     */

    private String preDatasource;

    /**
     * 数据源
     */

    private String prodDatasource;


    /**
     * 状态
     */

    private Integer apiOnlineStatus;


    /**
     * SQL模式下的执行脚本
     */

    private String script;
    /**
     * 服务
     */
    private String service;

    /**
     * 必填参数列表
     */
    private List<String> requestParamsList;

    /**
     * 返回体结构
     */
    private JSONObject responseFormat;

    /**
     * 其他参数
     */

    private JSONObject optionData;

    public ApiInfo() {
    }

    public ApiInfo(Long id, String apiName, String path, String method, String options, String type, String name,
                   String testDatasource, String preDatasource, String prodDatasource, Integer apiOnlineStatus,
                   String script, String service, List<String> requestParamsList, JSONObject responseFormat,
                    JSONObject optionData) {
        this.id = id;
        this.apiName = apiName;
        this.path = path;
        this.method = method;
        this.options = options;
        this.type = type;
        this.name = name;
        this.testDatasource = testDatasource;
        this.preDatasource = preDatasource;
        this.prodDatasource = prodDatasource;
        this.apiOnlineStatus = apiOnlineStatus;
        this.script = script;
        this.service = service;
        this.requestParamsList = requestParamsList;
        this.responseFormat = responseFormat;
        this.optionData = optionData;
    }

    public String getTestDatasource() {
        return testDatasource;
    }

    public void setTestDatasource(String testDatasource) {
        this.testDatasource = testDatasource;
    }

    public String getPreDatasource() {
        return preDatasource;
    }

    public void setPreDatasource(String preDatasource) {
        this.preDatasource = preDatasource;
    }

    public String getProdDatasource() {
        return prodDatasource;
    }

    public void setProdDatasource(String prodDatasource) {
        this.prodDatasource = prodDatasource;
    }

    public Integer getApiOnlineStatus() {
        return apiOnlineStatus;
    }

    public void setApiOnlineStatus(Integer apiOnlineStatus) {
        this.apiOnlineStatus = apiOnlineStatus;
    }

    public JSONObject getOptionData() {
        return optionData;
    }

    public void setOptionData(JSONObject optionData) {
        this.optionData = optionData;
    }

    private ApiInfo(Builder builder) {
        setId(builder.id);
        setApiName(builder.apiName);
        setPath(builder.path);
        setMethod(builder.method);
        setOptions(builder.options);
        setType(builder.type);
        setName(builder.name);
        setTestDatasource(builder.testDatasource);
        setPreDatasource(builder.preDatasource);
        setProdDatasource(builder.prodDatasource);
        setApiOnlineStatus(builder.apiOnlineStatus);
        setScript(builder.script);
        setService(builder.service);
        setRequestParamsList(builder.requestParamsList);
        setResponseFormat(builder.responseFormat);
        setOptionData(builder.optionData);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public List<String> getRequestParamsList() {
        return requestParamsList;
    }

    public void setRequestParamsList(List<String> requestParamsList) {
        this.requestParamsList = requestParamsList;
    }

    public JSONObject getResponseFormat() {
        return responseFormat;
    }

    public void setResponseFormat(JSONObject responseFormat) {
        this.responseFormat = responseFormat;
    }

    public static final class Builder {
        private Long id;
        private String apiName;
        private String path;
        private String method;
        private String options;
        private String type;
        private String name;
        private String testDatasource;
        private String preDatasource;
        private String prodDatasource;
        private Integer apiOnlineStatus;
        private String script;
        private String service;
        private List<String> requestParamsList;
        private JSONObject responseFormat;
        private JSONObject optionData;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(Long val) {
            id = val;
            return this;
        }


        public Builder apiName(String val) {
            apiName = val;
            return this;
        }

        public Builder path(String val) {
            path = val;
            return this;
        }

        public Builder method(String val) {
            method = val;
            return this;
        }

        public Builder options(String val) {
            options = val;
            return this;
        }

        public Builder type(String val) {
            type = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder testDatasource(String val) {
            testDatasource = val;
            return this;
        }

        public Builder preDatasource(String val) {
            preDatasource = val;
            return this;
        }

        public Builder prodDatasource(String val) {
            prodDatasource = val;
            return this;
        }

        public Builder apiOnlineStatus(Integer val) {
            apiOnlineStatus = val;
            return this;
        }


        public Builder script(String val) {
            script = val;
            return this;
        }

        public Builder service(String val) {
            service = val;
            return this;
        }

        public Builder responseFormat(JSONObject val) {
            responseFormat = val;
            return this;
        }

        public Builder optionData(JSONObject val) {
            optionData = val;
            return this;
        }

        public Builder requestParamsList(List<String> val) {
            requestParamsList = val;
            return this;
        }

        public ApiInfo build() {
            return new ApiInfo(this);
        }
    }

    @Override
    public String toString() {
        return "ApiInfo{" +
                "id=" + id +
                ", apiName='" + apiName + '\'' +
                ", path='" + path + '\'' +
                ", method='" + method + '\'' +
                ", options='" + options + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", script='" + script + '\'' +
                ", service='" + service + '\'' +
                ", requestParamsList=" + requestParamsList +
                ", responseFormat=" + responseFormat +
                '}';
    }
}
