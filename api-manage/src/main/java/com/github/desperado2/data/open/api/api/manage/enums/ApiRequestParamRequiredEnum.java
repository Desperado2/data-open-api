package com.github.desperado2.data.open.api.api.manage.enums;


/**
 * API参数是否必填枚举
 * @author tu nan
 * @date 2021/3/10
 **/
public enum ApiRequestParamRequiredEnum {

    /**
     * 下架
     */
    FALSE(0,"非必填"),

    /**
     * 上级
     */
    YES(1,"必填");


    /**
     * 状态码
     */
    private Integer code;

    /**
     * 状态名称
     */
    private String name;

    ApiRequestParamRequiredEnum(Integer code, String name) {
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
        for (ApiRequestParamRequiredEnum value : values()) {
            if(value.code.equals(code)){
                return true;
            }
        }
        return false;
    }
}
