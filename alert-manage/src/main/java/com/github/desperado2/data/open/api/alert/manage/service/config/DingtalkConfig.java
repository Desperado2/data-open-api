package com.github.desperado2.data.open.api.alert.manage.service.config;


import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 配置
 * @author tu nan
 * @date 2023/4/28
 **/
@Configuration
@ConfigurationProperties(prefix = "alert.dingtalk")
@ConditionalOnProperty(value = {"alert.dingtalk.access-token", "alert.dingtalk.secret"}, matchIfMissing = false)
public class DingtalkConfig {

    private String accessToken;

    private String secret;

    private String atUser;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getAtUser() {
        return atUser;
    }

    public void setAtUser(String atUser) {
        this.atUser = atUser;
    }
}
