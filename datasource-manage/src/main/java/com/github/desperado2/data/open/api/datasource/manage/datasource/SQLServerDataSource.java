package com.github.desperado2.data.open.api.datasource.manage.datasource;

import com.github.desperado2.data.open.api.datasource.manage.model.TableInfo;

import javax.sql.DataSource;
import java.util.List;
import java.util.regex.Pattern;

/**
 * sql server 数据源
 */
public class SQLServerDataSource extends JdbcDataSource {

    Pattern pattern = Pattern.compile("(order +by .*)",Pattern.CASE_INSENSITIVE);

    public SQLServerDataSource(String dataSourceCode, DataSource dataSource) {
        super(dataSourceCode,dataSource);
    }

    public SQLServerDataSource(DataSource dataSource, boolean storeApi) {
        super(dataSource, storeApi);
    }

    @Override
    public String transcoding(String param) {
        return param
                .replace("'","''");
    }

    @Override
    public List<TableInfo> buildTableInfo() {
        return null;
    }


}
