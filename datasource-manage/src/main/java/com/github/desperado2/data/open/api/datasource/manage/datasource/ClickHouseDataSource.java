package com.github.desperado2.data.open.api.datasource.manage.datasource;


import com.github.desperado2.data.open.api.datasource.manage.model.TableInfo;

import javax.sql.DataSource;
import java.util.List;

/**
 * mongodb 数据源操作
 */
public class ClickHouseDataSource extends JdbcDataSource {

    public ClickHouseDataSource(String dataSourceCode, DataSource dataSource) {
        super(dataSourceCode,dataSource);
    }



    @Override
    public String transcoding(String param) {
        return param
                .replace("\'","\\\'");
    }

    @Override
    public List<TableInfo> buildTableInfo() {
        return null;
    }
}
