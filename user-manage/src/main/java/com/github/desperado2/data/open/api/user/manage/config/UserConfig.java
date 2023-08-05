package com.github.desperado2.data.open.api.user.manage.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 用户信息配置
 * @author tu nan
 * @date 2023/4/18
 **/
@Configuration
public class UserConfig {

    @Value("${open.data.platform.user.email.suffix: null}")
    private String emailSuffix;

    @Value("${open.data.platform.user.token.refresh-token-end-time: 1800}")
    private Long tokenRefreshTokenEndTime;

    @Value("${open.data.platform.user.token.timeout: 7200}")
    private Long tokenTimeout;

    @Value("${open.data.platform.cookie.domain: 127.0.0.1}")
    private String cookieDomain;

    @Value("${open.data.platform.cookie.timeout: 7200}")
    private Long cookieTimeout;

    public String getEmailSuffix() {
        return emailSuffix;
    }

    public void setEmailSuffix(String emailSuffix) {
        this.emailSuffix = emailSuffix;
    }

    public Long getTokenRefreshTokenEndTime() {
        return tokenRefreshTokenEndTime;
    }

    public void setTokenRefreshTokenEndTime(Long tokenRefreshTokenEndTime) {
        this.tokenRefreshTokenEndTime = tokenRefreshTokenEndTime;
    }

    public Long getTokenTimeout() {
        return tokenTimeout;
    }

    public void setTokenTimeout(Long tokenTimeout) {
        this.tokenTimeout = tokenTimeout;
    }

    public String getCookieDomain() {
        return cookieDomain;
    }

    public void setCookieDomain(String cookieDomain) {
        this.cookieDomain = cookieDomain;
    }

    public Long getCookieTimeout() {
        return cookieTimeout;
    }

    public void setCookieTimeout(Long cookieTimeout) {
        this.cookieTimeout = cookieTimeout;
    }
}
