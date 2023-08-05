package com.github.desperado2.data.open.api.udf.manage.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;

/**
 * UDF定义
 * @author tu nan
 * @date 2023/5/22
 **/
@TableName("t_udf_definition")
public class UdfDefinition {

    @ApiModelProperty("id")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("UDF名称")
    private String name;

    @ApiModelProperty("UDF分类")
    private String type;

    @ApiModelProperty("UDF定义")
    private String definition;

    @ApiModelProperty("UDF参数")
    private String parameter;

    @ApiModelProperty("UDF返回值")
    private String returnValue;

    @ApiModelProperty("UDF描述")
    private String description;

    @ApiModelProperty("UDF示例")
    private String example;

    @ApiModelProperty("UDF状态")
    private Integer status;

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


    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

