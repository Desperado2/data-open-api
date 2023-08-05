package com.github.desperado2.data.open.api.log.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.desperado2.data.open.api.log.manage.entity.ApiLogs;
import com.github.desperado2.data.open.api.log.manage.model.ApiLogStatisticsModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 对外接口访问秘钥表 Mapper 接口
 *
 * @author jingjing.mu
 * @since 2021-01-27
 */
@Mapper
public interface ApiLogsMapper extends BaseMapper<ApiLogs> {

    @Select("SELECT DATE(create_time) AS date, COUNT(1) AS requireCount, SUM(IF(status=0, 1, 0)) AS successCount, " +
            "SUM(IF(status=1, 1, 0)) AS failCount FROM t_api_logs " +
            "WHERE DATE(create_time) >  DATE(DATE_SUB(now(), INTERVAL 7 day)) GROUP BY DATE(create_time) " +
            "ORDER BY date ASC ")
    List<ApiLogStatisticsModel> logStatistics();


    @Select("SELECT DATE(create_time) AS date, COUNT(1) AS requireCount, SUM(IF(status=0, 1, 0)) AS successCount, " +
            "SUM(IF(status=1, 1, 0)) AS failCount FROM t_api_logs " +
            "WHERE user_id = #{user_id} AND DATE(create_time) >  DATE(DATE_SUB(now(), INTERVAL 7 day)) GROUP BY DATE(create_time) " +
            "ORDER BY date ASC ")
    List<ApiLogStatisticsModel> logStatisticsByUser(Long userId);
}
