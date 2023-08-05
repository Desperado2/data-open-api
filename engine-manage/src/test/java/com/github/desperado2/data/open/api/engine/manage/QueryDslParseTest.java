package com.github.desperado2.data.open.api.engine.manage;


import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;

/**
 * 解析测试
 * @author tu nan
 * @date 2023/3/31
 **/
//@SpringBootTest
public class QueryDslParseTest {


    public static void main(String[] args) {
        String statement = "SELECT\n" +
                "\t ifnull(sum(num),0) num,ifnull(sum(alarm),0) alarm\n" +
                "FROM\n" +
                "\t`zhuanma` \n" +
                "<where>\n" +
                "1 = 1 and use_for_code not like \"01%\"\n" +
                "<if test=\"areaNumber != null and areaNumber !=''\">\n" +
                "AND area like concat(#{areaNumber},'%')\n" +
                "</if>\n" +
                "<if test=\"useCode != null and useCode !=''\">\n" +
                "AND use_for_code like concat(#{useCode},'%')\n" +
                "</if>\n" +
                "<if test=\"appId != null and appId !=''\">\n" +
                "AND app_id = #{appId}\n" +
                "</if>\n" +
                "<if test=\"startTime != null and startTime !='' \">\n" +
                "AND date >= #{startTime}\n" +
                "</if>\n" +
                "<if test=\"endTime != null and endTime !=''\">\n" +
                "AND date &lt;= #{endTime}\n" +
                "</if>\n" +
                "</where>";
        Configuration configuration = new Configuration();
        SqlSource sqlSource = configuration.getLanguageRegistry().getDefaultDriver().createSqlSource(configuration, statement, null);
        BoundSql boundSql = sqlSource.getBoundSql(null);
        for (ParameterMapping parameterMapping : boundSql.getParameterMappings()) {
            System.out.println(parameterMapping.getProperty());
        }
    }
}
