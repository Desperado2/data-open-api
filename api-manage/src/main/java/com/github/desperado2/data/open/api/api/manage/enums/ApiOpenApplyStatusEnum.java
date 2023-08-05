package com.github.desperado2.data.open.api.api.manage.enums;


/**
 * API开放申请状态
 * @author tu nan
 * @date 2021/3/10
 **/
public enum ApiOpenApplyStatusEnum {


    OPEN(0,"开放"),

    NOT_OPEN(1,"不开放");


    /**
     * 状态码
     */
    private Integer code;

    /**
     * 状态名称
     */
    private String name;

    ApiOpenApplyStatusEnum(Integer code, String name) {
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
        for (ApiOpenApplyStatusEnum value : values()) {
            if(value.code.equals(code)){
                return true;
            }
        }
        return false;
    }
}
