package com.github.desperado2.data.open.api.cache.manage.model;


import io.swagger.annotations.ApiModelProperty;

/**
 * 接口订阅数据库模型
 * @author tu nan
 * @date 2023/2/13
 **/

public class ApiApplyInfo {

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户名称")
    private String username;

    @ApiModelProperty("appId")
    private Long apiId;

    public ApiApplyInfo() {
    }

    public ApiApplyInfo(Long userId, String username, Long apiId) {
        this.userId = userId;
        this.apiId = apiId;
        this.username = username;
    }

    private ApiApplyInfo(Builder builder) {
        setUserId(builder.userId);
        setUsername(builder.username);
        setApiId(builder.apiId);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    public static final class Builder {
        private Long userId;
        private String username;
        private Long apiId;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder userId(Long val) {
            userId = val;
            return this;
        }

        public Builder username(String val) {
            username = val;
            return this;
        }

        public Builder apiId(Long val) {
            apiId = val;
            return this;
        }

        public ApiApplyInfo build() {
            return new ApiApplyInfo(this);
        }
    }

    @Override
    public String toString() {
        return "ApiApplyInfo{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", apiId=" + apiId +
                '}';
    }
}
