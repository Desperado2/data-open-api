package com.github.desperado2.data.open.api.api.manage.enums;


/**
 * API分类状态
 * @author tu nan
 * @date 2021/3/10
 **/
public enum ApiClassifyStatusEnum {

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

    ApiClassifyStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static boolean isExist(Integer code){
        for (ApiClassifyStatusEnum value : values()) {
            if(value.code.equals(code)){
                return true;
            }
        }
        return false;
    }
}
