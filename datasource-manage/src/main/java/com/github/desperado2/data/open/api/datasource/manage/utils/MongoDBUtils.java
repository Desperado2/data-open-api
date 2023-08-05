package com.github.desperado2.data.open.api.datasource.manage.utils;


import com.github.desperado2.data.open.api.datasource.manage.entity.DataSourceConfig;
import com.mongodb.MongoClientURI;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

/**
 * Mongo工具
 * @author tu nan
 * @date 2023/3/18
 **/
public class MongoDBUtils {

    /**
     * 获取mongo连接
     * @param config 配置
     * @return 连接
     */
    public static MongoTemplate getMongoTemplate(DataSourceConfig config){
        MongoClientURI mongoClientURI = new MongoClientURI(config.getUrl());
        MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(mongoClientURI);
        return new MongoTemplate(mongoDbFactory);
    }
}
