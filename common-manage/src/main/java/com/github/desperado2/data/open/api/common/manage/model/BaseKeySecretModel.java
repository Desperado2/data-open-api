package com.github.desperado2.data.open.api.common.manage.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 对外查询签名参数对象
 * @author tu nan
 * @date 2021/3/11
 **/
@ApiModel(value = "BaseKeySecretModel", description = "对外查询签名参数对象")
public class BaseKeySecretModel {


    @ApiModelProperty("appKey")
    private String appKey;

    @ApiModelProperty("请求时间，格式 yyyyMMddHHmmss")
    private String signTime;

    @ApiModelProperty("签名")
    private String sign;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getSignTime() {
        return signTime;
    }

    public void setSignTime(String signTime) {
        this.signTime = signTime;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
