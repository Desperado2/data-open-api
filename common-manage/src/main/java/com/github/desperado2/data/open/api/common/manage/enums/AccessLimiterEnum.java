package com.github.desperado2.data.open.api.common.manage.enums;

/**
 * 限流类型枚举
 * @author tu nan
 * @date 2021/3/11
 **/
public enum AccessLimiterEnum {

    /**
     * 根据APPKEY限流
     */
    APPKEY_LIMIT(0,"根据APPKEY限流"),

    /**
     * 根据IP地址限流
     */
    IP_LIMIT(1,"根据IP地址限流");

    /**
     * 限流类型码
     */
    private Integer code;

    /**
     * 限流类型名称
     */
    private String name;

    AccessLimiterEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
