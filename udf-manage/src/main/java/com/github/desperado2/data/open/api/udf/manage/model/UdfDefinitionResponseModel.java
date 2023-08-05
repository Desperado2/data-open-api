package com.github.desperado2.data.open.api.udf.manage.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * UDF定义
 * @author tu nan
 * @date 2023/5/22
 **/
@ApiModel(description = "UDF定义模型", value = "UdfDefinitionResponseModel")
public class UdfDefinitionResponseModel {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("UDF名称")
    private String name;

    @ApiModelProperty("UDF分类")
    private String type;

    @ApiModelProperty("UDF定义")
    private String definition;

    @ApiModelProperty("UDF参数")
    private List<UdfParamModel> parameter;

    @ApiModelProperty("UDF返回值")
    private UdfReturnModel returnValue;

    @ApiModelProperty("UDF描述")
    private String description;

    @ApiModelProperty("UDF示例")
    private String example;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public List<UdfParamModel> getParameter() {
        return parameter;
    }

    public void setParameter(List<UdfParamModel> parameter) {
        this.parameter = parameter;
    }

    public UdfReturnModel getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(UdfReturnModel returnValue) {
        this.returnValue = returnValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }
}

