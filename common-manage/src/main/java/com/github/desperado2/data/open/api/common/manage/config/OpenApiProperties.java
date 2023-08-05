package com.github.desperado2.data.open.api.common.manage.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 配置属性
 */
@Component
public class OpenApiProperties {

    /**
     * 基础注册路径
     */
    @Value("${open.data.platform.base.open-api-base-register-path:/open-api}")
    private String baseRegisterPath = "";

    @Value("${open.data.platform.base.open-api-service-address:http://127.0.0.1:15124}")
    private String serviceAddress = "";

    @Value("${open.data.platform.base.open-api-front-address:http://192.168.20.109:15124/}")
    private String serviceFrontAddress = "";

    /**
     * 驼峰自动转换配置，默认true
     */
    private boolean mapUnderscoreToCamelCase = true;

    /**
     * post传参 操作整个body部分的key值
     * 在脚本中执行return bodyRoot;将返回整个body对象
     */
    private String bodyRootKey = "bodyRoot";


    public OpenApiProperties() {
    }

    public OpenApiProperties(String baseRegisterPath,boolean mapUnderscoreToCamelCase, String bodyRootKey) {
        this.baseRegisterPath = baseRegisterPath;
        this.mapUnderscoreToCamelCase = mapUnderscoreToCamelCase;
        this.bodyRootKey = bodyRootKey;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public String getBaseRegisterPath() {
        return baseRegisterPath;
    }

    public void setBaseRegisterPath(String baseRegisterPath) {
        this.baseRegisterPath = baseRegisterPath;
    }

    public boolean isMapUnderscoreToCamelCase() {
        return mapUnderscoreToCamelCase;
    }

    public void setMapUnderscoreToCamelCase(boolean mapUnderscoreToCamelCase) {
        this.mapUnderscoreToCamelCase = mapUnderscoreToCamelCase;
    }

    public String getServiceFrontAddress() {
        return serviceFrontAddress;
    }

    public void setServiceFrontAddress(String serviceFrontAddress) {
        this.serviceFrontAddress = serviceFrontAddress;
    }

    public String getBodyRootKey() {
        return bodyRootKey;
    }

    public void setBodyRootKey(String bodyRootKey) {
        this.bodyRootKey = bodyRootKey;
    }
}
