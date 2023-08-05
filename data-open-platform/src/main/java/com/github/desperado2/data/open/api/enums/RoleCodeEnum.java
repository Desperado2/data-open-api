package com.github.desperado2.data.open.api.enums;


/**
 * 角色枚举
 * @author tu nan
 * @date 2021/3/10
 **/
public enum RoleCodeEnum {

    ADMIN("admin","管理员"),

    NORMAL("normal","普通用户");


    /**
     * 角色编码
     */
    private final String code;

    /**
     * 角色名称
     */
    private final String name;

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
