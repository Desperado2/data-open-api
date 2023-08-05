package com.github.desperado2.data.open.api.api.manage.model;


import com.github.desperado2.data.open.api.common.manage.model.PageSearch;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 开放API分类
 * @author tu nan
 * @date 2023/2/9
 **/
@ApiModel(value = "OpenApiClassifyListRequest", description = "分类列表查询")
public class OpenApiClassifyListRequest extends PageSearch {

    @ApiModelProperty("分类编码")
    private String code;

    @ApiModelProperty("分类名称")
    private String name;

    @ApiModelProperty("状态")
    private Integer status;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
