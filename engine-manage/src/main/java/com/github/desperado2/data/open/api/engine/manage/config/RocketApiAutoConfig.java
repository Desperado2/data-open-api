package com.github.desperado2.data.open.api.engine.manage.config;


import com.github.desperado2.data.open.api.common.manage.config.SpringContextUtils;
import com.github.desperado2.data.open.api.datasource.manage.datasource.DataSourceManager;
import com.github.desperado2.data.open.api.datasource.manage.factory.*;
import com.github.desperado2.data.open.api.engine.manage.ApiInfoContent;
import com.github.desperado2.data.open.api.engine.manage.RequestMappingService;
import com.github.desperado2.data.open.api.engine.manage.encrypt.DefaultScriptEncrypt;
import com.github.desperado2.data.open.api.engine.manage.encrypt.IScriptEncrypt;
import com.github.desperado2.data.open.api.engine.manage.function.*;
import com.github.desperado2.data.open.api.engine.manage.interceptor.DefaultSQLInterceptor;
import com.github.desperado2.data.open.api.engine.manage.interceptor.ISQLInterceptor;
import com.github.desperado2.data.open.api.engine.manage.result.DefaultResultWrapper;
import com.github.desperado2.data.open.api.engine.manage.result.IResultWrapper;
import com.github.desperado2.data.open.api.engine.manage.scripts.IScriptParse;
import com.github.desperado2.data.open.api.engine.manage.scripts.ScriptParseService;
import com.github.desperado2.data.open.api.engine.manage.scripts.mybatis.MyBatisScriptParse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 自动配置类
 */
@Configuration
@ConditionalOnBean(DataSourceManager.class)
public class RocketApiAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }

    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 1允许任何域名使用
        corsConfiguration.addAllowedOrigin("*");
        // 2允许任何头
        corsConfiguration.addAllowedHeader("*");
        // 3允许任何方法（post、get等）
        corsConfiguration.addAllowedMethod("*");
        return corsConfiguration;
    }


    @Bean
    @ConditionalOnMissingBean
    public QLRequestMappingFactory getQLRequestMappingFactory(){
        return new QLRequestMappingFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public ScriptParseService getScriptParseService(){
        return new ScriptParseService();
    }

    @Bean
    @ConditionalOnMissingBean
    public IResultWrapper getIResultWrapper(){
        return new DefaultResultWrapper();
    }
    @Bean
    @ConditionalOnMissingBean
    public IAssertException getDefaultAssertException(){
        return new DefaultAssertException();
    }

    @Bean
    @ConditionalOnMissingBean
    public IScriptParse getIScriptParse(){
        return new MyBatisScriptParse();
    }

    @Bean
    @ConditionalOnMissingBean
    public IScriptEncrypt getIScriptEncrypt(){
        return new DefaultScriptEncrypt();
    }

    @Bean
    @ConditionalOnMissingBean
    public ApiInfoContent getApiInfoContent(){
        return new ApiInfoContent();
    }

    @Bean
    @ConditionalOnMissingBean
    public AssertFunction getAssertFunction(){
        return new AssertFunction();
    }

    @Bean
    @ConditionalOnMissingBean
    public DbFunction getDbFunction(){
        return new DbFunction();
    }

    @Bean
    @ConditionalOnMissingBean
    public EsFunction getEsFunction(){
        return new EsFunction();
    }

    @Bean
    @ConditionalOnMissingBean
    public EnvFunction getEnvFunction(){
        return new EnvFunction();
    }

    @Bean
    @ConditionalOnMissingBean
    public LogFunction getLogFunction(){
        return new LogFunction();
    }

    @Bean
    @ConditionalOnMissingBean
    public DbValueFunction getDbValueFunction(){
        return new DbValueFunction();
    }

//    @Bean
//    @ConditionalOnMissingBean
//    public PagerFunction getPagerFunction(){
//        return new PagerFunction();
//    }

    @Bean
    @ConditionalOnMissingBean
    public UtilsFunction getUtilsFunction(){
        return new UtilsFunction();
    }

    @Bean
    @ConditionalOnMissingBean
    public DateFunction getDateFunction(){
        return new DateFunction();
    }

    @Bean
    @ConditionalOnMissingBean
    public ContextFunction getContextFunction(){
        return new ContextFunction();
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringContextUtils getSpringContextUtils(ApplicationContext applicationContext){
        return new SpringContextUtils(applicationContext);
    }

    @Bean
    @ConditionalOnMissingBean
    public ISQLInterceptor getSQLInterceptor(){
        return new DefaultSQLInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean
    public RequestMappingService getRequestMappingService(){
        return new RequestMappingService();
    }

    @Bean
    @ConditionalOnMissingBean
    public ClickHouseDriver getClickHouseDriver(){
        return new ClickHouseDriver();
    }

//    @Bean
//    @ConditionalOnMissingBean
//    public MongoDriver getMongoDriver(){
//        return new MongoDriver();
//    }

    @Bean
    @ConditionalOnMissingBean
    public MySQLDriver getMySQLDriver(){
        return new MySQLDriver();
    }

    @Bean
    @ConditionalOnMissingBean
    public OracleDriver getOracleDriver(){
        return new OracleDriver();
    }

    @Bean
    @ConditionalOnMissingBean
    public PostgreSQLDriver getPostgreSQLDriver(){
        return new PostgreSQLDriver();
    }

    @Bean
    @ConditionalOnMissingBean
    public SQLServerDriver getSQLServerDriver(){
        return new SQLServerDriver();
    }

}
