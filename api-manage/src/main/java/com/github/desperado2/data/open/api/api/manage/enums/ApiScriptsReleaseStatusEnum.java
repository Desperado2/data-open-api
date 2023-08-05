package com.github.desperado2.data.open.api.api.manage.enums;


/**
 * API状态
 * @author tu nan
 * @date 2021/3/10
 **/
public enum ApiScriptsReleaseStatusEnum {

    /**
     * 下架
     */
    PUBLISH(0,"发布"),

    /**
     * 上级
     */
    NOT_PUBLISH(1,"未发布");


    /**
     * 状态码
     */
    private Integer code;

    /**
     * 状态名称
     */
    private String name;

    ApiScriptsReleaseStatusEnum(Integer code, String name) {
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
        for (ApiScriptsReleaseStatusEnum value : values()) {
            if(value.code.equals(code)){
                return true;
            }
        }
        return false;
    }
}
