package com.github.desperado2.data.open.api.datasource.manage.factory;


import com.github.desperado2.data.open.api.datasource.manage.datasource.ClickHouseDataSource;
import com.github.desperado2.data.open.api.datasource.manage.datasource.DataSourceDialect;
import com.github.desperado2.data.open.api.datasource.manage.entity.DataSourceConfig;
import com.github.desperado2.data.open.api.datasource.manage.enums.DataSourceTypeEnum;
import org.springframework.stereotype.Component;

/**
 * SQL  构造器
 */
@Component
public class ClickHouseDriver extends JdbcDriver {

    @Override
    public String getName() {
        return DataSourceTypeEnum.CLICKHOUSE.getName();
    }

    @Override
    public String getIcon() {
        return DataSourceTypeEnum.CLICKHOUSE.getIcon();
    }

    @Override
    public String getFormat() {
        return DataSourceTypeEnum.CLICKHOUSE.getFormat();
    }

    @Override
    public DataSourceDialect factory(DataSourceConfig config) throws Exception {
        return new ClickHouseDataSource(config.getCode(),super.getDataSource(config));
    }
}
