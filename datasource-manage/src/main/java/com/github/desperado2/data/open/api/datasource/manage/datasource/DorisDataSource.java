package com.github.desperado2.data.open.api.datasource.manage.datasource;

import com.github.desperado2.data.open.api.datasource.manage.model.FieldInfo;
import com.github.desperado2.data.open.api.datasource.manage.model.TableInfo;
import com.github.desperado2.data.open.api.datasource.manage.utils.SqlUtils;

import javax.sql.DataSource;
import java.util.*;

/**
 * mysql 数据源
 */
public class DorisDataSource extends JdbcDataSource {


    public DorisDataSource(String dataSourceCode, DataSource dataSource) {
        super(dataSourceCode,dataSource);
    }

    public DorisDataSource(DataSource dataSource, boolean storeApi) {
        super(dataSource, storeApi);
    }


    @Override
    public String transcoding(String param) {
        return param
                .replace("\\","\\\\")
                .replace("\"","\\\"")
                .replace("\'","\\\'");
    }

    @Override
    public List<TableInfo> buildTableInfo(){
        try {
            List<TableInfo> tableInfos = new ArrayList<>();
            List<Map<String,Object>> tables = jdbcTemplate.queryForList("show tables",Collections.EMPTY_MAP);
            for (Map<String,Object> table : tables){
                Set<String> keys = table.keySet();
                String tableName = table.get(keys.toArray(new String[]{})[0]).toString();
                Map<String,Object> fields = jdbcTemplate.queryForMap("show create table "+tableName,Collections.EMPTY_MAP);

                //只处理逻辑表
                if (fields.get("Create Table") == null){
                    continue;
                }
                String tableInfo = fields.get("Create Table").toString();
                String tableComment = SqlUtils.getByPattern(tableInfo, "\\) .* COMMENT='(.*)'", 1);
                List<FieldInfo> fieldInfos = new ArrayList<>();
                tableInfos.add(new TableInfo(tableName, tableComment, fieldInfos));
                List<String> fieldStrList = SqlUtils.getColumnSqls(tableInfo);
                for (String oneLine : fieldStrList) {
                    String fieldName = SqlUtils.getByPattern(oneLine, "`(.*)`", 1);
                    String fieldComment = SqlUtils.getByPattern(oneLine, "COMMENT '(.*)'", 1);
                    String fieldType = SqlUtils.getByPattern(oneLine, "`" + fieldName + "` ([A-Za-z]*)", 1);
                    fieldInfos.add(new FieldInfo(fieldName, fieldComment, fieldType));
                }
            }
            return tableInfos;
        }catch (Exception e){
            e.printStackTrace();
        }
        return Collections.emptyList();

    }
}
