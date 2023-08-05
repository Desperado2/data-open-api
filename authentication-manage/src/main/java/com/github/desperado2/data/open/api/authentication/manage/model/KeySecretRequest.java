package com.github.desperado2.data.open.api.authentication.manage.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 对外接口访问秘钥表
 * </p>
 *
 * @author jingjing.mu
 * @since 2021-01-27
 */
@ApiModel(value="KeySecretRequest", description="防伪对外接口访问秘钥表")
public class KeySecretRequest implements Serializable {

    @ApiModelProperty(value = "对外密钥的key")
    @NotBlank(message = "APPKEY不能为空")
    @Length(max = 32, message = "APPKEY长度最大为32位")
    private String appKey;


    @ApiModelProperty(value = "对外密钥值")
    @NotBlank(message = "APPSECRET不能为空")
    @Length(min = 32, max = 64, message = "APPSECRET长度必须在32到64之间")
    private String appSecret;


    @ApiModelProperty(value = "对应的用户id")
    @NotBlank(message = "用户ID不能为空")
    private Long userId;


    @ApiModelProperty(value = "状态，0-使用中,1-未启用，2-已禁用")
    private Integer status;

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
}
