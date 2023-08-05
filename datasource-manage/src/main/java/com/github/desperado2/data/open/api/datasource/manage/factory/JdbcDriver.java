package com.github.desperado2.data.open.api.datasource.manage.factory;


import com.github.desperado2.data.open.api.datasource.manage.entity.DataSourceConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

/**
 * 数据驱动
 * @author tu nan
 * @date 2023/3/9
 **/
public abstract class JdbcDriver extends IDataSourceDialectDriver{

    /**
     * 获取数据源
     * @param dataSourceConfig 数据源配置
     * @return 数据源
     */
    protected DataSource getDataSource(DataSourceConfig dataSourceConfig){
        HikariConfig hikariConfig = new HikariConfig(dataSourceConfig.getProperties());
        hikariConfig.setConnectionTimeout(30000);
        hikariConfig.setIdleTimeout(60000);
        hikariConfig.setValidationTimeout(3000);
        hikariConfig.setMaxLifetime(120000);
        hikariConfig.setMaximumPoolSize(60);
        hikariConfig.setMinimumIdle(10);
        hikariConfig.addHealthCheckProperty("connectivityCheckTimeoutMs", "1000");
        hikariConfig.setAutoCommit(true);
        hikariConfig.setConnectionTestQuery("SELECT 1");
        hikariConfig.setLeakDetectionThreshold(60 * 1000);
        hikariConfig.setPoolName(dataSourceConfig.getCode());
        hikariConfig.setJdbcUrl(dataSourceConfig.getUrl());
        hikariConfig.setUsername(dataSourceConfig.getUsername());
        hikariConfig.setPassword(dataSourceConfig.getPassword());
        return new HikariDataSource(hikariConfig);
    }
}
