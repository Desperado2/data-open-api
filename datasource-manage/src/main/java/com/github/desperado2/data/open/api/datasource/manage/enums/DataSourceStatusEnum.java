package com.github.desperado2.data.open.api.datasource.manage.enums;

/**
 * 数据源状态枚举
 *
 * @author tu nan
 * @date 2023/3/9
 **/
public enum DataSourceStatusEnum {
    ENABLE(0,"开启"),
    DISABLE(1,"禁用");

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 状态名称
     */
    private final String name;

    DataSourceStatusEnum(Integer code, String name) {
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
