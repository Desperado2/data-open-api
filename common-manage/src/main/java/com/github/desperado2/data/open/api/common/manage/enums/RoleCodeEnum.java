package com.github.desperado2.data.open.api.common.manage.enums;


/**
 * API订阅状态
 * @author tu nan
 * @date 2021/3/10
 **/
public enum RoleCodeEnum {

    /**
     * 待审核
     */
    ADMIN("admin","管理员"),

    /**
     * 正常
     */
    NORMAL("normal","普通用户");


    /**
     * 状态码
     */
    private String code;

    /**
     * 状态名称
     */
    private String name;

    RoleCodeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }


}
