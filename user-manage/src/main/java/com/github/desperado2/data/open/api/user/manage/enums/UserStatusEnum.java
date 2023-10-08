package com.github.desperado2.data.open.api.user.manage.enums;


/**
 * 用户状态
 * @author tu nan
 * @date 2021/3/10
 **/
public enum UserStatusEnum {


    DEACTIVATE(0,"停用"),


    NORMAL(1,"正常"),


    NOT_ACTIVATED(2,"未激活");


    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 状态名称
     */
    private final String name;

    UserStatusEnum(Integer code, String name) {
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
