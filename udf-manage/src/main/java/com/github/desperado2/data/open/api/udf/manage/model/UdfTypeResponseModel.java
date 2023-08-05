package com.github.desperado2.data.open.api.udf.manage.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * UDF定义
 * @author tu nan
 * @date 2023/5/22
 **/
@ApiModel(description = "UDF类型模型", value = "UdfTypeResponseModel")
public class UdfTypeResponseModel {

    @ApiModelProperty("UDF分类")
    private String type;

    @ApiModelProperty("UDF列表")
    private List<UdfDefinitionResponseModel> udfList;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<UdfDefinitionResponseModel> getUdfList() {
        return udfList;
    }

    public void setUdfList(List<UdfDefinitionResponseModel> udfList) {
        this.udfList = udfList;
    }
}

