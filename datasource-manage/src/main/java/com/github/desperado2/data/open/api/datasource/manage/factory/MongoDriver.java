//package com.github.desperado2.open.data.platform.datasource.manage.factory;
//
//import com.github.desperado2.open.data.platform.datasource.manage.datasource.DataSourceDialect;
//import com.github.desperado2.open.data.platform.datasource.manage.datasource.MongoDataSource;
//import com.github.desperado2.open.data.platform.datasource.manage.entity.DataSourceConfig;
//import com.github.desperado2.open.data.platform.datasource.manage.enums.DataSourceTypeEnum;
//import com.github.desperado2.open.data.platform.datasource.manage.utils.MongoDBUtils;
//import org.springframework.stereotype.Component;
//
///**
// * mongodb  构造器
// */
//@Component
//public class MongoDriver extends IDataSourceDialectDriver {
//
//    @Override
//    public String getName() {
//        return DataSourceTypeEnum.MongoDB.getName();
//    }
//
//    @Override
//    public String getIcon() {
//        return DataSourceTypeEnum.MongoDB.getIcon();
//    }
//
//    @Override
//    public String getFormat() {
//        return DataSourceTypeEnum.MongoDB.getFormat();
//    }
//
//    @Override
//    public DataSourceDialect factory(DataSourceConfig config) {
//        return new MongoDataSource(MongoDBUtils.getMongoTemplate(config));
//    }
//}
