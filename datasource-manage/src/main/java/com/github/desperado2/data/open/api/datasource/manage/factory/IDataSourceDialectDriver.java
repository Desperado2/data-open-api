package com.github.desperado2.data.open.api.datasource.manage.factory;


import com.github.desperado2.data.open.api.datasource.manage.datasource.DataSourceDialect;
import com.github.desperado2.data.open.api.datasource.manage.entity.DataSourceConfig;

import java.io.Serializable;

/**
 * 数据源方言
 * @author tu nan
 * @date 2023/3/9
 **/
public abstract class IDataSourceDialectDriver implements Serializable {

    public abstract String getName();

    public abstract String getIcon();

    public abstract String getFormat();

    public String getDriver(){
        return this.getClass().getName();
    }

    public abstract DataSourceDialect factory(DataSourceConfig config) throws Exception;
}
