package com.github.desperado2.data.open.api.model.statistic;


/**
 * 基本统计信息 模型
 * @author tu nan
 * @date 2023/3/22
 **/
public class BaseInfoStatisticsModel {

    private Integer apiCount;

    private Integer userCount;

    private Integer subscribeCount;

    private Integer callCount;

    private Integer successCount;

    private Integer failCount;

    public Integer getApiCount() {
        return apiCount;
    }

    public void setApiCount(Integer apiCount) {
        this.apiCount = apiCount;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public Integer getSubscribeCount() {
        return subscribeCount;
    }

    public void setSubscribeCount(Integer subscribeCount) {
        this.subscribeCount = subscribeCount;
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
