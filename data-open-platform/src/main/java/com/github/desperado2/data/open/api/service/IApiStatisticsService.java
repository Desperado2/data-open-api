package com.github.desperado2.data.open.api.service;

import com.github.desperado2.data.open.api.model.statistic.BaseInfoStatisticsModel;
import com.github.desperado2.data.open.api.model.statistic.RequestInfoStatisticsModel;

import java.util.List;

/**
 * 订阅接口
 *
 * @author tu nan
 * @date 2023/2/13
 **/
public interface IApiStatisticsService {


    /**
     * 基础统计
     * @return 统计
     * @throws Exception 异常
     */
    BaseInfoStatisticsModel baseStatistics() throws Exception;

    /**
     * 调用日志统计
     * @return 统计
     */
    List<RequestInfoStatisticsModel> requestStatistics();
}
