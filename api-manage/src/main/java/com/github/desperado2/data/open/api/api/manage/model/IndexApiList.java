package com.github.desperado2.data.open.api.api.manage.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 开放API分类
 * @author tu nan
 * @date 2023/2/9
 **/
@ApiModel(value = "IndexApiList", description = "首页API信息")
public class IndexApiList {

    @ApiModelProperty("分类id")
    private Long id;

    @ApiModelProperty("分类编码")
    private String code;

    @ApiModelProperty("分类名称")
    private String name;

    @ApiModelProperty("分类描述")
    private String description;

    @ApiModelProperty("分类显示顺序")
    private Integer showIndex;

    @ApiModelProperty("API数量")
    private Integer apiCount;

    @ApiModelProperty("API列表")
    private List<OpenApiResponseModel> apiList;

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

    public Integer getApiCount() {
        return apiCount;
    }

    public void setApiCount(Integer apiCount) {
        this.apiCount = apiCount;
    }

    public List<OpenApiResponseModel> getApiList() {
        return apiList;
    }

    public void setApiList(List<OpenApiResponseModel> apiList) {
        this.apiList = apiList;
    }
}
