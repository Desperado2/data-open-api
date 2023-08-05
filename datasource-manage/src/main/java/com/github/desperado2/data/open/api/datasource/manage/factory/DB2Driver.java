package com.github.desperado2.data.open.api.datasource.manage.factory;


import com.github.desperado2.data.open.api.datasource.manage.datasource.DataSourceDialect;
import com.github.desperado2.data.open.api.datasource.manage.entity.DataSourceConfig;
import com.github.desperado2.data.open.api.datasource.manage.enums.DataSourceTypeEnum;
import org.springframework.stereotype.Component;

/**
 * MYSQL驱动
 * @author tu nan
 * @date 2023/3/9
 **/
@Component
public class DB2Driver extends JdbcDriver{
    @Override
    public String getName() {
        return DataSourceTypeEnum.MYSQL.getName();
    }

    @Override
    public String getIcon() {
        return DataSourceTypeEnum.MYSQL.getIcon();
    }

    @Override
    public String getFormat() {
        return DataSourceTypeEnum.MYSQL.getFormat();
    }

    @Override
    public DataSourceDialect factory(DataSourceConfig config) throws Exception {
        return null;
    }
}
