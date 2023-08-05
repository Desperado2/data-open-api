package com.github.desperado2.data.open.api.datasource.manage.model;

/**
 * 数据源信息模型
 * @author tu nan
 * @date 2023/3/31
 **/
public class DataSourceInfoModel {

    /**
     * 类型
     */
    private Integer code;

    /**
     * 名称
     */
    private String name;

    /**
     * 图标
     */
    private String icon;

    /**
     * 连接格式
     */
    private String format;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
