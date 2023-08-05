package com.github.desperado2.data.open.api.datasource.manage.factory;

import com.github.desperado2.data.open.api.datasource.manage.datasource.DataSourceDialect;
import com.github.desperado2.data.open.api.datasource.manage.datasource.OracleDataSource;
import com.github.desperado2.data.open.api.datasource.manage.entity.DataSourceConfig;
import com.github.desperado2.data.open.api.datasource.manage.enums.DataSourceTypeEnum;
import org.springframework.stereotype.Component;

@Component
public class OracleDriver extends JdbcDriver {

    @Override
    public String getName() {
        return DataSourceTypeEnum.ORACLE.getName();
    }

    @Override
    public String getIcon() {
        return DataSourceTypeEnum.ORACLE.getIcon();
    }

    @Override
    public String getFormat() {
        return DataSourceTypeEnum.ORACLE.getFormat();
    }

    @Override
    public DataSourceDialect factory(DataSourceConfig config) throws Exception {
        return new OracleDataSource(config.getCode(),super.getDataSource(config));
    }
}
