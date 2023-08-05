package com.github.desperado2.data.open.api.cache.manage.model;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 对外接口访问秘钥表
 *
 * @author jingjing.mu
 * @since 2021-01-27
 */
public class KeySecretInfo implements Serializable {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "对外密钥的key")
    private String appKey;

    @ApiModelProperty(value = "对外密钥值")
    private String appSecret;

    @ApiModelProperty(value = "对应的用户id")
    private Long userId;

    public KeySecretInfo() {
    }

    public KeySecretInfo(Long id, String appKey, String appSecret, Long userId) {
        this.id = id;
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.userId = userId;
    }

    private KeySecretInfo(Builder builder) {
        setId(builder.id);
        setAppKey(builder.appKey);
        setAppSecret(builder.appSecret);
        setUserId(builder.userId);
    }

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

    public static final class Builder {
        private Long id;
        private String appKey;
        private String appSecret;
        private Long userId;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(Long val) {
            id = val;
            return this;
        }

        public Builder appKey(String val) {
            appKey = val;
            return this;
        }

        public Builder appSecret(String val) {
            appSecret = val;
            return this;
        }

        public Builder userId(Long val) {
            userId = val;
            return this;
        }

        public KeySecretInfo build() {
            return new KeySecretInfo(this);
        }
    }
}
