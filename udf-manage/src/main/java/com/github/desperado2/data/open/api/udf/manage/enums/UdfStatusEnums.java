package com.github.desperado2.data.open.api.udf.manage.enums;


/**
 * 状态枚举
 * @author tu nan
 * @date 2023/5/22
 **/
public enum UdfStatusEnums {

    /**
     * 下架
     */
    ENABLE(0,"下架"),

    /**
     * 上级
     */
    DISABLE(1,"上级");


    /**
     * 状态码
     */
    private Integer code;

    /**
     * 状态名称
     */
    private String name;

    UdfStatusEnums(Integer code, String name) {
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
