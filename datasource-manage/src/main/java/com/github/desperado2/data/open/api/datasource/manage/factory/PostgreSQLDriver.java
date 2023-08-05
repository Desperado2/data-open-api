package com.github.desperado2.data.open.api.datasource.manage.factory;

import com.github.desperado2.data.open.api.datasource.manage.datasource.DataSourceDialect;
import com.github.desperado2.data.open.api.datasource.manage.datasource.PostgreSQLDataSource;
import com.github.desperado2.data.open.api.datasource.manage.entity.DataSourceConfig;
import com.github.desperado2.data.open.api.datasource.manage.enums.DataSourceTypeEnum;
import org.springframework.stereotype.Component;

/**
 * postgre SQL  构造器
 */
@Component
public class PostgreSQLDriver extends JdbcDriver {

    @Override
    public String getName() {
        return DataSourceTypeEnum.POSTGRE_SQL.getName();
    }

    @Override
    public String getIcon() {
        return DataSourceTypeEnum.POSTGRE_SQL.getIcon();
    }

    @Override
    public String getFormat() {
        return DataSourceTypeEnum.POSTGRE_SQL.getFormat();
    }

    @Override
    public DataSourceDialect factory(DataSourceConfig config) throws Exception {
        return new PostgreSQLDataSource(config.getCode(),super.getDataSource(config));
    }
}
