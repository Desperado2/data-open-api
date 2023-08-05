package com.github.desperado2.data.open.api.authentication.manage.enums;


/**
 * 用户状态
 * @author tu nan
 * @date 2021/3/10
 **/
public enum KeySecretFieldEnum {

    APP_KEY("appKey","key"),

    SIGN_TIME("signTime","签名时间"),

    SIGN("sign","签名");


    /**
     * 状态码
     */
    private final String code;

    /**
     * 状态名称
     */
    private final String name;

    KeySecretFieldEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }


    /**
     * 判断枚举状态是否存在
     * @param code 状态码code
     * @return true存在，false不存在
     */
    public static boolean isExist(String code){
        KeySecretFieldEnum[] values = KeySecretFieldEnum.values();
        for (KeySecretFieldEnum value : values) {
            if(value.getCode().equals(code)){
                return true;
            }
        }
        return false;
    }
}
