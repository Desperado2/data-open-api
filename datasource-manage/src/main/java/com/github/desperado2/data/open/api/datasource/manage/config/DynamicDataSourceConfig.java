package com.github.desperado2.data.open.api.datasource.manage.config;


import com.github.desperado2.data.open.api.datasource.manage.datasource.DataSourceDialect;
import com.github.desperado2.data.open.api.datasource.manage.service.IDataSourceConfigService;
import com.github.desperado2.data.open.api.datasource.manage.service.datasource.DefaultDataSourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 数据源配置信息
 * @author tu nan
 * @date 2023/3/10
 **/
@Component
public class DynamicDataSourceConfig {

    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceConfig.class);

    private final IDataSourceConfigService dataSourceConfigService;

    private final DefaultDataSourceManager dataSourceManager;

    public DynamicDataSourceConfig(IDataSourceConfigService dataSourceConfigService, DefaultDataSourceManager dataSourceManager) {
        this.dataSourceConfigService = dataSourceConfigService;
        this.dataSourceManager = dataSourceManager;
    }

    @PostConstruct
    public void  init(){
        // 加载数据源
        try {
            Map<String, DataSourceDialect> dialectMap = new LinkedHashMap<>();
            dataSourceManager.setDialectMap(dialectMap);
            dataSourceConfigService.loadAllDataSource();
        }catch (Exception e){
            logger.error("数据源加载异常", e);
        }
    }
}
