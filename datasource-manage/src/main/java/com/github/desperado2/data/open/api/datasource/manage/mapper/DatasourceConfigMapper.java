package com.github.desperado2.data.open.api.datasource.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.desperado2.data.open.api.datasource.manage.entity.DataSourceConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * 接口映射
 *
 * @author tu nan
 * @date 2023/3/9
 **/
@Mapper
public interface DatasourceConfigMapper extends BaseMapper<DataSourceConfig> {
}
