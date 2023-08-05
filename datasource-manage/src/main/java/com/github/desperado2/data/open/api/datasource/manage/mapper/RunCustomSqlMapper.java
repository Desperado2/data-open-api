package com.github.desperado2.data.open.api.datasource.manage.mapper;

import java.util.List;
import java.util.Map;

/**
 * 运行自定义SQL
 *
 * @author tu nan
 * @date 2023/3/14
 **/
public interface RunCustomSqlMapper {

    List<Map<String, Object>> selectList(Map<String, Object> params);
}
