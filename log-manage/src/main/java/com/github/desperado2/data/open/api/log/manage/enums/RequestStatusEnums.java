package com.github.desperado2.data.open.api.log.manage.enums;



/**
 * 请求状态枚举
 * @author tu nan
 * @date 2023/2/13
 **/
public enum RequestStatusEnums {


    SUCCESS(0,"成功"),


    FAIL(1,"失败");

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 状态名称
     */
    private final String name;

    RequestStatusEnums(Integer code, String name) {
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
