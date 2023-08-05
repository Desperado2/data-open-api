package com.github.desperado2.data.open.api.udf.manage.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * UDF定义
 * @author tu nan
 * @date 2023/5/22
 **/
@ApiModel(description = "UDF返回值模型", value = "UdfReturnModel")
public class UdfReturnModel {

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("说明")
    private String desc;

    @ApiModelProperty("UDF")
    private String example;


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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }
}

