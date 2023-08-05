package com.github.desperado2.data.open.api.enums;


/**
 * API订阅状态
 * @author tu nan
 * @date 2021/3/10
 **/
public enum ApiSubscribeStatusEnum {


    WAIT_APPROVAL(0,"待审核"),


    NORMAL(1,"正常"),


    REFUSE(2,"拒绝"),


    DISABLE(3,"禁用"),


    CANCEL(4,"取消");


    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 状态名称
     */
    private final String name;

    ApiSubscribeStatusEnum(Integer code, String name) {
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
