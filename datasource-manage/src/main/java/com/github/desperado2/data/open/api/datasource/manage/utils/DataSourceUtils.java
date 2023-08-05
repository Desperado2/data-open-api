package com.github.desperado2.data.open.api.datasource.manage.utils;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.IOException;

/**
 * 数据源工具
 * @author tu nan
 * @date 2023/3/18
 **/
public class DataSourceUtils {

    /**
     * 关闭数据源
     * @param dataSource 数据源
     */
    public static void closeDataSource(DataSource dataSource){
        if (dataSource == null){
            return;
        }
        if (!(dataSource instanceof Closeable)){
            return;
        }
        try {
            ((Closeable)dataSource).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
