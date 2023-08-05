package com.github.desperado2.data.open.api.model.statistic;


/**
 * 基本统计信息 模型
 * @author tu nan
 * @date 2023/3/22
 **/
public class RequestInfoStatisticsModel {

    private String date;

    private Integer callCount;

    private Integer successCount;

    private Integer failCount;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getCallCount() {
        return callCount;
    }

    public void setCallCount(Integer callCount) {
        this.callCount = callCount;
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
