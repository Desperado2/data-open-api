package com.github.desperado2.data.open.api.common.manage.enums;


/**
 * API订阅状态
 * @author tu nan
 * @date 2021/3/10
 **/
public enum RoleStatusEnum {

    /**
     * 正常
     */
    NORMAL(0,"正常"),

    /**
     * 禁用
     */
    FORBID(1,"禁用");


    /**
     * 状态码
     */
    private Integer code;

    /**
     * 状态名称
     */
    private String name;

    RoleStatusEnum(Integer code, String name) {
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
