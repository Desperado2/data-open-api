package com.github.desperado2.data.open.api.datasource.manage.factory;


import com.github.desperado2.data.open.api.datasource.manage.datasource.DataSourceDialect;
import com.github.desperado2.data.open.api.datasource.manage.datasource.PrestoDataSource;
import com.github.desperado2.data.open.api.datasource.manage.entity.DataSourceConfig;
import com.github.desperado2.data.open.api.datasource.manage.enums.DataSourceTypeEnum;
import org.springframework.stereotype.Component;

/**
 * Presto  构造器
 */
@Component
public class PrestoDriver extends JdbcDriver {

    @Override
    public String getName() {
        return DataSourceTypeEnum.PRESTO.getName();
    }

    @Override
    public String getIcon() {
        return DataSourceTypeEnum.PRESTO.getIcon();
    }

    @Override
    public String getFormat() {
        return DataSourceTypeEnum.PRESTO.getFormat();
    }

    @Override
    public DataSourceDialect factory(DataSourceConfig config) {
        return new PrestoDataSource(config.getCode(),super.getDataSource(config));
    }
}
