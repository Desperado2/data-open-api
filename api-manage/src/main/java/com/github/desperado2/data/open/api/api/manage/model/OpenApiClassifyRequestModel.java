
package com.github.desperado2.data.open.api.api.manage.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 开放API分类
 * @author tu nan
 * @date 2023/2/9
 **/
@ApiModel(description = "菜单分类的模型")
public class OpenApiClassifyRequestModel {

    @ApiModelProperty("Id")
    private Long id;

    @ApiModelProperty("分类编码")
    private String code;

    @ApiModelProperty("分类名称")
    private String name;

    @ApiModelProperty("分类描述")
    private String description;

    @ApiModelProperty("分类显示顺序")
    private Integer showIndex;

    @ApiModelProperty("状态")
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getShowIndex() {
        return showIndex;
    }

    public void setShowIndex(Integer showIndex) {
        this.showIndex = showIndex;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
