package com.github.desperado2.data.open.api.authentication.manage.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 对外接口访问秘钥表
 *
 * @author jingjing.mu
 * @since 2021-01-27
 */
@ApiModel(value="SignResultModel", description="签名结果模型")
public class SignResultModel implements Serializable {

    @ApiModelProperty(value = "签名字符串")
    private String signStr;

    @ApiModelProperty(value = "签名")
    private String sign;

    public SignResultModel() {
    }

    public SignResultModel(String signStr, String sign) {
        this.signStr = signStr;
        this.sign = sign;
    }

    public String getSignStr() {
        return signStr;
    }

    public void setSignStr(String signStr) {
        this.signStr = signStr;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
