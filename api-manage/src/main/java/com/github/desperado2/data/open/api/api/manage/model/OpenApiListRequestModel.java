package com.github.desperado2.data.open.api.api.manage.model;


import com.github.desperado2.data.open.api.common.manage.model.PageSearch;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 开发API模型
 * @author tu nan
 * @date 2023/2/9
 **/
@ApiModel(value = "OpenApiListRequestModel", description = "API模型")
public class OpenApiListRequestModel extends PageSearch {

    @ApiModelProperty("API名称")
    private String name;

    @ApiModelProperty("API分类")
    private String classifyName;

    @ApiModelProperty("API状态")
    private Integer status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
