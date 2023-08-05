package com.github.desperado2.data.open.api.authentication.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 防伪对外接口访问秘钥表
 * </p>
 *
 * @author jingjing.mu
 * @since 2021-01-27
 */
@TableName("t_key_secret")
@ApiModel(value="KeySecret对象", description="防伪对外接口访问秘钥表")
public class KeySecret implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "对外密钥的key")
    private String appKey;

    @ApiModelProperty(value = "对外密钥值")
    private String appSecret;

    @ApiModelProperty(value = "对应的用户id")
    private Long userId;

    @ApiModelProperty(value = "状态，0-使用中,1-未启用，2-已禁用")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public KeySecret() {
    }

    public KeySecret(Long id, Integer status, Date updateTime) {
        this.id = id;
        this.status = status;
        this.updateTime = updateTime;
    }
}
