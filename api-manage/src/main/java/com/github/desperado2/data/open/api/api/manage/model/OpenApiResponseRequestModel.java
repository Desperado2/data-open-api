package com.github.desperado2.data.open.api.api.manage.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 开发API模型
 * @author tu nan
 * @date 2023/2/9
 **/
@ApiModel(description = "API响应模型")
public class OpenApiResponseRequestModel {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("APIid")
    private Long apiId;

    @ApiModelProperty("参数名称")
    private String name;

    @ApiModelProperty("参数类型")
    private String type;

    @ApiModelProperty("示例值")
    private String exampleValue;

    @ApiModelProperty("说明")
    private String description;

    public OpenApiResponseRequestModel() {
    }

    public OpenApiResponseRequestModel(String name, String type, String exampleValue, String description) {
        this.name = name;
        this.type = type;
        this.exampleValue = exampleValue;
        this.description = description;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExampleValue() {
        return exampleValue;
    }

    public void setExampleValue(String exampleValue) {
        this.exampleValue = exampleValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}