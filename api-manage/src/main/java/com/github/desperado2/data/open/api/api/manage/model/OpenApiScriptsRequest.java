package com.github.desperado2.data.open.api.api.manage.model;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 开发API模型
 * @author tu nan
 * @date 2023/2/9
 **/
public class OpenApiScriptsRequest {

    @ApiModelProperty("id")
    @TableId
    private Long id;

    @ApiModelProperty("API的ID")
    private Long apiId;

    @ApiModelProperty("API运行环境")
    private String apiRunEnvironment;

    @ApiModelProperty("数据源编码")
    private String testDatasourceCode;

    @ApiModelProperty("数据源编码")
    private String preDatasourceCode;

    @ApiModelProperty("数据源编码")
    private String prodDatasourceCode;

    @ApiModelProperty("API脚本类型")
    private String apiScriptType;

    @ApiModelProperty("API脚本")
    private String apiScript;

    @ApiModelProperty("API脚本状态")
    private Integer apiScriptStatus;

    @ApiModelProperty("API请求头")
    private List<Map<String, Object>> apiRequestHeader;

    @ApiModelProperty("API请求体")
    private Map<String, Object> apiRequestBody;

    @ApiModelProperty("API响应头")
    private List<Map<String, Object>> apiResponseHeader;

    @ApiModelProperty("API响应体")
    private Map<String, Object> apiResponseBody;

    @ApiModelProperty("API响应体格式")
    private JSONObject apiResponseFormat;

    @ApiModelProperty("API扩展信息")
    private Map<String, Object> apiOption;

    @ApiModelProperty("是否自定义返回结构")
    private Integer customResultStructure;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

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

    public String getApiRunEnvironment() {
        return apiRunEnvironment;
    }

    public void setApiRunEnvironment(String apiRunEnvironment) {
        this.apiRunEnvironment = apiRunEnvironment;
    }

    public String getTestDatasourceCode() {
        return testDatasourceCode;
    }

    public void setTestDatasourceCode(String testDatasourceCode) {
        this.testDatasourceCode = testDatasourceCode;
    }

    public String getPreDatasourceCode() {
        return preDatasourceCode;
    }

    public void setPreDatasourceCode(String preDatasourceCode) {
        this.preDatasourceCode = preDatasourceCode;
    }

    public String getProdDatasourceCode() {
        return prodDatasourceCode;
    }

    public void setProdDatasourceCode(String prodDatasourceCode) {
        this.prodDatasourceCode = prodDatasourceCode;
    }

    public String getApiScriptType() {
        return apiScriptType;
    }

    public void setApiScriptType(String apiScriptType) {
        this.apiScriptType = apiScriptType;
    }

    public String getApiScript() {
        return apiScript;
    }

    public void setApiScript(String apiScript) {
        this.apiScript = apiScript;
    }

    public Integer getApiScriptStatus() {
        return apiScriptStatus;
    }

    public void setApiScriptStatus(Integer apiScriptStatus) {
        this.apiScriptStatus = apiScriptStatus;
    }

    public List<Map<String, Object>> getApiRequestHeader() {
        return apiRequestHeader;
    }

    public void setApiRequestHeader(List<Map<String, Object>> apiRequestHeader) {
        this.apiRequestHeader = apiRequestHeader;
    }

    public Map<String, Object> getApiRequestBody() {
        return apiRequestBody;
    }

    public void setApiRequestBody(Map<String, Object> apiRequestBody) {
        this.apiRequestBody = apiRequestBody;
    }

    public List<Map<String, Object>> getApiResponseHeader() {
        return apiResponseHeader;
    }

    public void setApiResponseHeader(List<Map<String, Object>> apiResponseHeader) {
        this.apiResponseHeader = apiResponseHeader;
    }

    public Map<String, Object> getApiResponseBody() {
        return apiResponseBody;
    }

    public void setApiResponseBody(Map<String, Object> apiResponseBody) {
        this.apiResponseBody = apiResponseBody;
    }

    public JSONObject getApiResponseFormat() {
        return apiResponseFormat;
    }

    public void setApiResponseFormat(JSONObject apiResponseFormat) {
        this.apiResponseFormat = apiResponseFormat;
    }

    public Map<String, Object> getApiOption() {
        return apiOption;
    }

    public void setApiOption(Map<String, Object> apiOption) {
        this.apiOption = apiOption;
    }

    public Integer getCustomResultStructure() {
        return customResultStructure;
    }

    public void setCustomResultStructure(Integer customResultStructure) {
        this.customResultStructure = customResultStructure;
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
