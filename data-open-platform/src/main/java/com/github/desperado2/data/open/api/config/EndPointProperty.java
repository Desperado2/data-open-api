package com.github.desperado2.data.open.api.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * 属性映射
 * @author tu nan
 * @date 2023/4/12
 **/
@Component
@Validated
@ConfigurationProperties(prefix = "open.data.platform.base", ignoreInvalidFields = true, ignoreUnknownFields = true)
public class EndPointProperty {

    @Value("${spring.redis.host:null}")
    private String redisHost = null;

    @Value("${spring.redis.port:-1}")
    private Integer redisPort = -1;

    private boolean accessLimiterOpen;

    private String accessLimiterType;

    private String openApiBaseRegisterPath;

    private String cacheType;

    private String openApiServiceAddress;

    private Boolean openSwagger;

    public String getRedisHost() {
        return redisHost;
    }

    public void setRedisHost(String redisHost) {
        this.redisHost = redisHost;
    }

    public Integer getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(Integer redisPort) {
        this.redisPort = redisPort;
    }

    public boolean isAccessLimiterOpen() {
        return accessLimiterOpen;
    }

    public void setAccessLimiterOpen(boolean accessLimiterOpen) {
        this.accessLimiterOpen = accessLimiterOpen;
    }

    public Boolean getAccessLimiterOpen() {
        return accessLimiterOpen;
    }

    public void setAccessLimiterOpen(Boolean accessLimiterOpen) {
        this.accessLimiterOpen = accessLimiterOpen;
    }

    public String getAccessLimiterType() {
        return accessLimiterType;
    }

    public void setAccessLimiterType(String accessLimiterType) {
        this.accessLimiterType = accessLimiterType;
    }

    public String getOpenApiBaseRegisterPath() {
        return openApiBaseRegisterPath;
    }

    public void setOpenApiBaseRegisterPath(String openApiBaseRegisterPath) {
        this.openApiBaseRegisterPath = openApiBaseRegisterPath;
    }

    public String getCacheType() {
        return cacheType;
    }

    public void setCacheType(String cacheType) {
        this.cacheType = cacheType;
    }

    public String getOpenApiServiceAddress() {
        return openApiServiceAddress;
    }

    public void setOpenApiServiceAddress(String openApiServiceAddress) {
        this.openApiServiceAddress = openApiServiceAddress;
    }


    public Boolean getOpenSwagger() {
        return openSwagger;
    }

    public void setOpenSwagger(Boolean openSwagger) {
        this.openSwagger = openSwagger;
    }
}
