package com.github.desperado2.data.open.api.datasource.manage.model;


import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.Properties;

/**
 * 数据源配置实体
 * @author tu nan
 * @date 2023/3/9
 **/
public class DataSourceList {

    @ApiModelProperty("Id")
    private Long id;

    @ApiModelProperty("类型")
    private Integer type;

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("是否启用")
    private Integer enabled;

    @ApiModelProperty("配置")
    private Properties properties;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
