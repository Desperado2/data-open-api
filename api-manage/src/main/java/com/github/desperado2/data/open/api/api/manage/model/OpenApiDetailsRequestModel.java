package com.github.desperado2.data.open.api.api.manage.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 开发API模型
 * @author tu nan
 * @date 2023/2/9
 **/
@ApiModel(description = "API详情模型")
public class OpenApiDetailsRequestModel {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("APIid")
    private Long apiId;

    @ApiModelProperty("API详细描述")
    private String detailDescription;

    @ApiModelProperty("API地址")
    private String apiAddress;

    @ApiModelProperty("协议")
    private String protocol;

    @ApiModelProperty("请求方式")
    private String requestMode;

    @ApiModelProperty("返回格式")
    private String returnFormat;

    @ApiModelProperty("返回示例")
    private String returnExample;

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

    public String getDetailDescription() {
        return detailDescription;
    }

    public void setDetailDescription(String detailDescription) {
        this.detailDescription = detailDescription;
    }

    public String getApiAddress() {
        return apiAddress;
    }

    public void setApiAddress(String apiAddress) {
        this.apiAddress = apiAddress;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getRequestMode() {
        return requestMode;
    }

    public void setRequestMode(String requestMode) {
        this.requestMode = requestMode;
    }

    public String getReturnFormat() {
        return returnFormat;
    }

    public void setReturnFormat(String returnFormat) {
        this.returnFormat = returnFormat;
    }

    public String getReturnExample() {
        return returnExample;
    }

    public void setReturnExample(String returnExample) {
        this.returnExample = returnExample;
    }
}
