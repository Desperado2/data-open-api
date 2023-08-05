package com.github.desperado2.data.open.api.datasource.manage.datasource;


import com.github.desperado2.data.open.api.datasource.manage.model.TableInfo;

import javax.sql.DataSource;
import java.util.List;

/**
 * oracle 数据源
 */
public class OracleDataSource extends JdbcDataSource {


    public OracleDataSource(String dataSourceCode, DataSource dataSource) {
        super(dataSourceCode,dataSource);
    }

    public OracleDataSource(DataSource dataSource, boolean storeApi) {
        super(dataSource, storeApi);
    }



    @Override
    public String transcoding(String param) {
        return param
                .replace("'", "''");
    }

    @Override
    public List<TableInfo> buildTableInfo() {
        return null;
    }
}
