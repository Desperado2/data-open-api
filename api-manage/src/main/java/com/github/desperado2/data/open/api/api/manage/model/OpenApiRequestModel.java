package com.github.desperado2.data.open.api.api.manage.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 开发API模型
 * @author tu nan
 * @date 2023/2/9
 **/
@ApiModel(description = "API模型")
public class OpenApiRequestModel {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("API名称")
    private String name;

    @ApiModelProperty("API描述")
    private String description;

    @ApiModelProperty("API分类")
    private Long classifyId;

    @ApiModelProperty("是否开放申请")
    private Integer openApplyStatus;

    @ApiModelProperty("不开放申请原因描述")
    private String notOpenApplyReason;

    @ApiModelProperty("API图片")
    private String imageUrl;

    private OpenApiDetailsRequestModel openApiDetailsRequestModel;

    private List<OpenApiRequestParamsRequestModel> openApiRequestParamsRequestModelList;

    private List<OpenApiResponseRequestModel> openApiResponseRequestModelList;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(Long classifyId) {
        this.classifyId = classifyId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getOpenApplyStatus() {
        return openApplyStatus;
    }

    public void setOpenApplyStatus(Integer openApplyStatus) {
        this.openApplyStatus = openApplyStatus;
    }

    public String getNotOpenApplyReason() {
        return notOpenApplyReason;
    }

    public void setNotOpenApplyReason(String notOpenApplyReason) {
        this.notOpenApplyReason = notOpenApplyReason;
    }

    public OpenApiDetailsRequestModel getOpenApiDetailsRequestModel() {
        return openApiDetailsRequestModel;
    }

    public void setOpenApiDetailsRequestModel(OpenApiDetailsRequestModel openApiDetailsRequestModel) {
        this.openApiDetailsRequestModel = openApiDetailsRequestModel;
    }

    public List<OpenApiRequestParamsRequestModel> getOpenApiRequestParamsRequestModelList() {
        return openApiRequestParamsRequestModelList;
    }

    public void setOpenApiRequestParamsRequestModelList(List<OpenApiRequestParamsRequestModel> openApiRequestParamsRequestModelList) {
        this.openApiRequestParamsRequestModelList = openApiRequestParamsRequestModelList;
    }

    public List<OpenApiResponseRequestModel> getOpenApiResponseRequestModelList() {
        return openApiResponseRequestModelList;
    }

    public void setOpenApiResponseRequestModelList(List<OpenApiResponseRequestModel> openApiResponseRequestModelList) {
        this.openApiResponseRequestModelList = openApiResponseRequestModelList;
    }
}
