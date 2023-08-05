package com.github.desperado2.data.open.api.datasource.manage.factory;


import com.github.desperado2.data.open.api.datasource.manage.datasource.DataSourceDialect;
import com.github.desperado2.data.open.api.datasource.manage.datasource.SQLServerDataSource;
import com.github.desperado2.data.open.api.datasource.manage.entity.DataSourceConfig;
import com.github.desperado2.data.open.api.datasource.manage.enums.DataSourceTypeEnum;
import org.springframework.stereotype.Component;

/**
 * SQL  构造器
 */
@Component
public class SQLServerDriver extends JdbcDriver {

    @Override
    public String getName() {
        return DataSourceTypeEnum.SQLSERVER.getName();
    }

    @Override
    public String getIcon() {
        return DataSourceTypeEnum.SQLSERVER.getIcon();
    }

    @Override
    public String getFormat() {
        return DataSourceTypeEnum.SQLSERVER.getFormat();
    }

    @Override
    public DataSourceDialect factory(DataSourceConfig config) throws Exception {
        return new SQLServerDataSource(config.getCode(),super.getDataSource(config));
    }
}
