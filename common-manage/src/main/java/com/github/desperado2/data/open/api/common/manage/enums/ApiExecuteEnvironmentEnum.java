package com.github.desperado2.data.open.api.common.manage.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * API执行环境枚举
 *
 * @author tu nan
 * @date 2023/4/7
 **/
public enum ApiExecuteEnvironmentEnum {

    TEST("test", "测试环境"),
    PRE("pre", "预发布环境"),
    PROD("prod", "正式环境");


    /**
     * code
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    ApiExecuteEnvironmentEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static String getNameByCode(String code){
        if(StringUtils.isBlank(code)){
            return String.format("未知-%s", code);
        }
        for (ApiExecuteEnvironmentEnum value : ApiExecuteEnvironmentEnum.values()) {
            if(value.code.equals(code)){
                return value.name + "-" + value.code;
            }
        }
        return String.format("未知-%s", code);
    }
}
