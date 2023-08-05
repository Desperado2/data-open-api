package com.github.desperado2.data.open.api.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 接口订阅数据库模型
 * @author tu nan
 * @date 2023/2/13
 **/
@ApiModel("订阅数据模型")
@TableName("t_api_subscribe")
public class ApiSubscribe {

    @ApiModelProperty("主键id")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("appId")
    private Long apiId;

    @ApiModelProperty("订阅状态")
    private Integer subscribeStatus;

    @ApiModelProperty("拒绝原因")
    private String refuseMessage;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date  updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    public Integer getSubscribeStatus() {
        return subscribeStatus;
    }

    public void setSubscribeStatus(Integer subscribeStatus) {
        this.subscribeStatus = subscribeStatus;
    }

    public String getRefuseMessage() {
        return refuseMessage;
    }

    public void setRefuseMessage(String refuseMessage) {
        this.refuseMessage = refuseMessage;
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
