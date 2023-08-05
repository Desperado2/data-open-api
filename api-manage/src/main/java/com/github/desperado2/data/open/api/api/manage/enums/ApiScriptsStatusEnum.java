package com.github.desperado2.data.open.api.api.manage.enums;


/**
 * API状态
 * @author tu nan
 * @date 2021/3/10
 **/
public enum ApiScriptsStatusEnum {

    /**
     * 下架
     */
    SAVE(0,"保存"),

    /**
     * 上级
     */
    EXECUTE(1,"执行"),

    SMOKE_TEST(2,"冒烟"),

    PUBLISH(3,"发布"),

    OFFLINE(4,"下架");


    /**
     * 状态码
     */
    private Integer code;

    /**
     * 状态名称
     */
    private String name;

    ApiScriptsStatusEnum(Integer code, String name) {
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
        for (ApiScriptsStatusEnum value : values()) {
            if(value.code.equals(code)){
                return true;
            }
        }
        return false;
    }
}
