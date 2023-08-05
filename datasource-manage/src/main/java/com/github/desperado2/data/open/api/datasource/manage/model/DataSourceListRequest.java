package com.github.desperado2.data.open.api.datasource.manage.model;


import com.github.desperado2.data.open.api.common.manage.model.PageInfo;

/**
 * 数据源配置实体
 * @author tu nan
 * @date 2023/3/9
 **/
public class DataSourceListRequest extends PageInfo {

    /**
     * 类型
     */
    private Integer type;

    /**
     * 名称
     */
    private String name;

    /**
     * 状态
     */
    private Integer enabled;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }
}
