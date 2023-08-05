package com.github.desperado2.data.open.api.api.manage.enums;


/**
 * API状态
 * @author tu nan
 * @date 2021/3/10
 **/
public enum ApiCustomResponseStructureEnum {

    /**
     * 下架
     */
    CLOSE(0,"未开启"),

    /**
     * 上级
     */
    OPEN(1,"开启");


    /**
     * 状态码
     */
    private Integer code;

    /**
     * 状态名称
     */
    private String name;

    ApiCustomResponseStructureEnum(Integer code, String name) {
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
        for (ApiCustomResponseStructureEnum value : values()) {
            if(value.code.equals(code)){
                return true;
            }
        }
        return false;
    }
}
