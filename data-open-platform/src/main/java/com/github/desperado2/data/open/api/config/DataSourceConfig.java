package com.github.desperado2.data.open.api.config;


import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 多数据源数据库连接池配置1
 *
 * @author chenyu
 * @date 2019-04-15
 */
@Configuration
public class DataSourceConfig {

    @Bean
    public GlobalConfig globalConfiguration() {
        GlobalConfig conf = new GlobalConfig();
        // 是否打印
        conf.setBanner(false)
                // 逻辑注入sql
                .setSqlInjector(new LogicSqlInjector())
                .setDbConfig(new GlobalConfig.DbConfig()
                .setLogicDeleteValue("1")
                .setLogicNotDeleteValue("0")
                  // 使用数据库生成方式
                .setIdType(IdType.AUTO));
        return conf;
    }

    /**
     * 分页插件，高级功能自行拓展
     *
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    @Bean
    public PlatformTransactionManager openTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * 配置一个web监控的filter
     * @return 过滤器
     */
    @Bean
    public FilterRegistrationBean<Filter> webStatFilter(){
        FilterRegistrationBean<Filter> beanFilter = new FilterRegistrationBean<>();
        beanFilter.setFilter(new WebStatFilter());
        Map<String,String> initParams = new HashMap<>(2);
        initParams.put("exclusions","*.js,*.css,/druid/*");
        beanFilter.setInitParameters(initParams);
        beanFilter.setUrlPatterns(Collections.singletonList("/*"));
        return  beanFilter;
    }

    /**
     * 把StatViewServlet注册到ServletRegistrationBean注册器
     * @return 拦截
     */
    @Bean
    public ServletRegistrationBean<Servlet> setStatViewServlet(){
        ServletRegistrationBean<Servlet> beanServlet = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");
        Map<String,String> initParams = new HashMap<>(8);
        initParams.put("loginUsername","openPlatformD");
        initParams.put("loginPassword","open123NEOP&1213");
        // 可以限制只能本地访问
        initParams.put("allow","127.0.0.1");
        beanServlet.setInitParameters(initParams);
        return  beanServlet;
    }
}