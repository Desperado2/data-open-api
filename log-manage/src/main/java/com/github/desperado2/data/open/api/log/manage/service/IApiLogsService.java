package com.github.desperado2.data.open.api.log.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.model.PageResults;
import com.github.desperado2.data.open.api.log.manage.entity.ApiLogs;
import com.github.desperado2.data.open.api.log.manage.model.ApiLogSearchModel;
import com.github.desperado2.data.open.api.log.manage.model.ApiLogStatisticsModel;
import com.github.desperado2.data.open.api.log.manage.model.ApiLogsModel;

import java.util.List;

/**
 * 日志接口
 *
 * @author tu nan
 * @date 2023/2/9
 **/
public interface IApiLogsService extends IService<ApiLogs> {

    /**
     * 新增日志
     * @param apiLogsModel 日志信息
     */
    void add(ApiLogsModel apiLogsModel);

    /**
     * 根据日志key获取日志
     * @param logKey 日志key
     * @return 日志
     */
    ApiLogs getByKey(String logKey);

    /**
     * 获取API的日志
     * @param apiId api id
     * @return 日志列表
     */
    PageResults<List<ApiLogsModel>> selectByApi(Long apiId);

    /**
     * 获取用户的日志
     * @param apiLogSearchModel 查询参数
     * @return 日志
     */
    PageResults<List<ApiLogsModel>> listByUser(ApiLogSearchModel apiLogSearchModel);


    /**
     * 查询所有的日志
     * @param apiLogSearchModel 参数
     * @return 日志列表
     * @throws DataOpenPlatformException 异常
     */
    PageResults<List<ApiLogsModel>> listAll(ApiLogSearchModel apiLogSearchModel) throws DataOpenPlatformException;

    /**
     * 日志数量
     * @return 数量
     */
    Integer normalCount();

    /**
     * 成功数量
     * @return 数量
     */
    Integer successCount();

    /**
     * 失败数量
     * @return 数量
     */
    Integer failCount();

    /**
     * 日志数量
     * @return 数量
     */
    Integer normalCountByUserId(Long userId);

    /**
     * 成功数量
     * @return 数量
     */
    Integer successCountByUserId(Long userId);

    /**
     * 失败数量
     * @return 数量
     */
    Integer failCountByUserId(Long userId);


    /**
     * 日志统计
     * @return 统计
     */
    List<ApiLogStatisticsModel> logStatistics();


    List<ApiLogStatisticsModel> logStatisticsByUserId(Long userId);
}
