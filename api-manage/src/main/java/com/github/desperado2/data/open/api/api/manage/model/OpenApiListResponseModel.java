package com.github.desperado2.data.open.api.api.manage.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 开发API模型
 * @author tu nan
 * @date 2023/2/9
 **/
@ApiModel(value = "OpenApiListResponseModel", description = "API模型")
public class OpenApiListResponseModel {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("API名称")
    private String name;

    @ApiModelProperty("API描述")
    private String description;

    @ApiModelProperty("API分类名称")
    private String classifyName;

    @ApiModelProperty("API分类Id")
    private Long classifyId;

    @ApiModelProperty("是否开放申请")
    private Integer openApplyStatus;

    @ApiModelProperty("不开放申请原因描述")
    private String notOpenApplyReason;

    @ApiModelProperty("API图片")
    private String imageUrl;

    @ApiModelProperty("API状态")
    private Integer status;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

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

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }

    public Long getClassifyId() {
        return classifyId;
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

    public void setClassifyId(Long classifyId) {
        this.classifyId = classifyId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
