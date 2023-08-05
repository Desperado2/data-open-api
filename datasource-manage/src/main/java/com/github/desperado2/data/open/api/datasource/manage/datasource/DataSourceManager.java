package com.github.desperado2.data.open.api.datasource.manage.datasource;


import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 数据源管理代理类
 * @author tu nan
 * @date 2023/3/9
 **/
public abstract class DataSourceManager {

    private Map<String, DataSourceDialect> dialectMap;


    public Map<String, DataSourceDialect> getDialectMap() {
        return dialectMap;
    }

    public void setDialectMap(Map<String, DataSourceDialect> dialectMap) {
        this.dialectMap = dialectMap;
    }

    public DataSourceDialect getDataSourceDialect(String datasource, String specifyDataSource){
        String dataSourceKey = StringUtils.isEmpty(specifyDataSource)?datasource:specifyDataSource;
        DataSourceDialect dataSourceDialect = this.dialectMap.get(dataSourceKey);
        if (dataSourceDialect == null){
            throw new IllegalArgumentException("unknown datasource `"+dataSourceKey+"`");
        }

        return dataSourceDialect;
    }
}
