package com.github.desperado2.data.open.api.datasource.manage.datasource;


import com.alibaba.fastjson.JSONObject;
import com.github.desperado2.data.open.api.datasource.manage.mapper.RunCustomSqlMapper;
import com.github.desperado2.data.open.api.datasource.manage.model.ScriptContext;
import com.github.desperado2.data.open.api.datasource.manage.model.TableInfo;
import com.github.desperado2.data.open.api.datasource.manage.utils.DataSourceUtils;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.type.JdbcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * jdbc 数据源
 * @author tu nan
 * @date 2023/3/9
 **/
public class JdbcDataSource extends DataSourceDialect implements DialectTransactionManager{

    private static final Logger logger = LoggerFactory.getLogger(JdbcDataSource.class);

    protected DataSource dataSource;

    private String dataSourceCode;

    protected NamedParameterJdbcTemplate jdbcTemplate;

    protected PlatformTransactionManager transactionManager;


    protected JdbcDataSource(){}

    public JdbcDataSource(String dataSourceCode, DataSource dataSource) {
        this.dataSourceCode = dataSourceCode;
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.transactionManager = new DataSourceTransactionManager(dataSource);
        this.dataSource = dataSource;
    }

    public JdbcDataSource(DataSource dataSource, boolean storeApi) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.transactionManager = new DataSourceTransactionManager(dataSource);
        this.dataSource = dataSource;
        this.setStoreApi(storeApi);
    }


    @Override
    public PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }

    @Override
    @Transactional
    public List<Map> find(ScriptContext scriptContext) throws Exception {
        SqlSession sqlSession = null;
        try {
            Configuration configuration = new Configuration(new Environment(dataSourceCode, new JdbcTransactionFactory(), dataSource));
            configuration.addMapper(RunCustomSqlMapper.class);
            // 返回值为null的列
            configuration.setCallSettersOnNulls(true);
            // 返回值全为null的行
            configuration.setReturnInstanceForEmptyRow(true);
            configuration.setJdbcTypeForNull(JdbcType.NULL);
            SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(configuration);
            String mapperId = createMapper(sqlSessionFactory, scriptContext.getScript().toString());
            sqlSession = sqlSessionFactory.openSession();
            String statement = String.format("com.github.desperado2.open.data.platform.datasource.manage.mapper.%s", mapperId);
            LinkedHashMap<String, Object> bindParams = scriptContext.getBindParams();
            List<Map<String, Object>> resultList = sqlSession.selectList(statement + ".selectList", bindParams);
            return resultList.stream().map(this::toReplaceKeyLow).collect(Collectors.toList());
        }catch (Exception e){
            logger.error("数据库连接异常",e);
            throw e;
        }finally {
            // 卸载
            if(sqlSession != null){
                sqlSession.close();
            }
        }
    }

    @Override
    public JSONObject findEs(ScriptContext scriptContext) throws Exception {
        throw new RuntimeException("不支持的方法");
    }

    @Override
    public Map findOne(ScriptContext scriptContext) throws Exception {
        List<Map> resultList = find(scriptContext);
        return resultList.stream().map(this::toReplaceKeyLow).findFirst().orElse(null);
    }

    @Override
    public String transcoding(String param) {
        return param.replace("\'","\\\'");
    }

    @Override
    public List<TableInfo> buildTableInfo() {
        return null;
    }

    @Override
    public void close() {
         DataSourceUtils.closeDataSource(dataSource);
    }

    @Override
    public String getLimitSql(int page, int pageSize) {
        return null;
    }


}
