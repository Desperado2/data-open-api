package com.github.desperado2.data.open.api.log.manage.model;


import io.swagger.annotations.ApiModelProperty;

/**
 * 日志统计模型
 * @author tu nan
 * @date 2023/3/22
 **/
public class ApiLogStatisticsModel {

    @ApiModelProperty("日期")
    private String date;

    @ApiModelProperty("数量")
    private Integer requireCount;

    @ApiModelProperty("成功数量")
    private Integer successCount;

    @ApiModelProperty("失败数量")
    private Integer failCount;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getRequireCount() {
        return requireCount;
    }

    public void setRequireCount(Integer requireCount) {
        this.requireCount = requireCount;
    }

    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    public Integer getFailCount() {
        return failCount;
    }

    public void setFailCount(Integer failCount) {
        this.failCount = failCount;
    }
}
