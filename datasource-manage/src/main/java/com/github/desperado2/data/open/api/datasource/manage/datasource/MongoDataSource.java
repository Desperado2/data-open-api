//package com.github.desperado2.open.data.platform.datasource.manage.datasource;
//
//
//import com.alibaba.fastjson.JSONObject;
//import com.github.desperado2.open.data.platform.datasource.manage.model.ScriptContext;
//import com.github.desperado2.open.data.platform.datasource.manage.model.TableInfo;
//import org.springframework.data.mongodb.core.MongoTemplate;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * mongodb 数据源操作
// */
//@SuppressWarnings("ALL")
//public class MongoDataSource extends DataSourceDialect {
//
//    private MongoTemplate mongoTemplate;
//
//
//    private MongoDataSource(){}
//
//    public MongoDataSource(MongoTemplate mongoTemplate) {
//        this.mongoTemplate = mongoTemplate;
//    }
//
//    public MongoDataSource(MongoTemplate mongoTemplate,boolean storeApi) {
//        this.mongoTemplate = mongoTemplate;
//        this.setStoreApi(storeApi);
//    }
//
//
//    @Override
//    public List<Map> find(ScriptContext scriptContext) throws Exception {
//        return null;
//    }
//
//    @Override
//    public JSONObject findEs(ScriptContext scriptContext) throws Exception {
//        return null;
//    }
//
//    @Override
//    public Map findOne(ScriptContext scriptContext) throws Exception {
//        return null;
//    }
//
//    @Override
//    public String transcoding(String param) {
//        return param
//                .replace("\\","\\\\")
//                .replace("\"","\\\"")
//                .replace("\'","\\\'");
//    }
//
//    @Override
//    public void close() {
//
//    }
//
//    @Override
//    public List<TableInfo> buildTableInfo() {
//        return null;
//    }
//}
