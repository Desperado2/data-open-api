package com.github.desperado2.data.open.api.datasource.manage.utils;


import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * MYBATIS 工具类
 * @author tu nan
 * @date 2023/7/11
 **/
public class MyBatisUtils {

    public static List<String> getParameterMappingList(String sql){
        ArrayList<String> parameterList = new ArrayList<>();
        Configuration configuration = new Configuration();
        SqlSource sqlSource = configuration.getLanguageRegistry().getDefaultDriver().createSqlSource(configuration, sql, null);
        BoundSql boundSql = sqlSource.getBoundSql(null);
        for (ParameterMapping parameterMapping : boundSql.getParameterMappings()) {
            parameterList.add(parameterMapping.getProperty());
        }
        return parameterList;
    }

    public static String getCountSQL(String sql){
        return "SELECT COUNT(1) t FROM (" + sql +") t";
    }
}
