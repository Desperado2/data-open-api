package com.github.desperado2.data.open.api.authentication.manage.model;

import com.github.desperado2.data.open.api.common.manage.model.PageSearch;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 对外接口访问秘钥表
 * </p>
 *
 * @author jingjing.mu
 * @since 2021-01-27
 */

@ApiModel(value="KeySecretQueryRequest", description="对外接口访问秘钥表")
public class KeySecretQueryRequest extends PageSearch {

    @ApiModelProperty(value = "对外密钥的key")
    private String appKey;

    @ApiModelProperty(value = "用户")
    private String username;

    @ApiModelProperty(value = "状态，0-禁用,1-启用")
    private Integer status;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
