package com.github.desperado2.data.open.api.datasource.manage.datasource;


import com.github.desperado2.data.open.api.datasource.manage.model.TableInfo;

import javax.sql.DataSource;
import java.util.List;

/**
 * db2 数据源
 */
public class DB2DataSource extends JdbcDataSource {

    public DB2DataSource(String dataSourceCode, DataSource dataSource) {
        super(dataSourceCode,dataSource);
    }

    public DB2DataSource(DataSource dataSource, boolean storeApi) {
       super(dataSource,storeApi);
    }



    @Override
    public List<TableInfo> buildTableInfo() {
        return null;
    }
}
