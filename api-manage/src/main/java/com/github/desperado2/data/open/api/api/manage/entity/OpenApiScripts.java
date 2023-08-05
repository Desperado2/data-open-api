package com.github.desperado2.data.open.api.api.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 开发API模型
 * @author tu nan
 * @date 2023/2/9
 **/
@TableName("t_open_api_scripts")
public class OpenApiScripts {

    @ApiModelProperty("id")
    @TableId(type = IdType.AUTO)
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
    private String apiRequestHeader;

    @ApiModelProperty("API请求体")
    private String apiRequestBody;

    @ApiModelProperty("API响应头")
    private String apiResponseHeader;

    @ApiModelProperty("API响应体")
    private String apiResponseBody;

    @ApiModelProperty("API响应体格式")
    private String apiResponseFormat;

    @ApiModelProperty("API扩展信息")
    private String apiOption;

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

    public String getApiRequestHeader() {
        return apiRequestHeader;
    }

    public void setApiRequestHeader(String apiRequestHeader) {
        this.apiRequestHeader = apiRequestHeader;
    }

    public String getApiRequestBody() {
        return apiRequestBody;
    }

    public void setApiRequestBody(String apiRequestBody) {
        this.apiRequestBody = apiRequestBody;
    }

    public String getApiResponseHeader() {
        return apiResponseHeader;
    }

    public void setApiResponseHeader(String apiResponseHeader) {
        this.apiResponseHeader = apiResponseHeader;
    }

    public String getApiResponseBody() {
        return apiResponseBody;
    }

    public void setApiResponseBody(String apiResponseBody) {
        this.apiResponseBody = apiResponseBody;
    }

    public String getApiResponseFormat() {
        return apiResponseFormat;
    }

    public void setApiResponseFormat(String apiResponseFormat) {
        this.apiResponseFormat = apiResponseFormat;
    }

    public String getApiOption() {
        return apiOption;
    }

    public void setApiOption(String apiOption) {
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
