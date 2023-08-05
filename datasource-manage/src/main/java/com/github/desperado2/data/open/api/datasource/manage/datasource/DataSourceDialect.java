package com.github.desperado2.data.open.api.datasource.manage.datasource;


import com.alibaba.fastjson.JSONObject;
import com.github.desperado2.data.open.api.common.manage.utils.FieldUtils;
import com.github.desperado2.data.open.api.common.manage.utils.UUIDUtils;
import com.github.desperado2.data.open.api.datasource.manage.model.ScriptContext;
import com.github.desperado2.data.open.api.datasource.manage.model.TableInfo;
import com.github.desperado2.data.open.api.datasource.manage.utils.MapperUtils;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据源方言抽象类
 * @author tu nan
 * @date 2023/3/9
 **/
public abstract class DataSourceDialect {

    private final static Logger logger = LoggerFactory.getLogger(DataSourceDialect.class);

    private boolean isDynamic = false;

    private boolean storeApi = false;

    public boolean isDynamic() {
        return isDynamic;
    }

    public void setDynamic(boolean dynamic) {
        isDynamic = dynamic;
    }

    public boolean isStoreApi() {
        return storeApi;
    }

    public void setStoreApi(boolean storeApi) {
        this.storeApi = storeApi;
    }


    //查询对象
    public abstract List<Map> find(ScriptContext scriptContext) throws Exception;

    public abstract JSONObject findEs(ScriptContext scriptContext) throws Exception;

    public abstract Map findOne(ScriptContext scriptContext) throws Exception;

    /**
     * 替换key
     */
    protected Map<String,Object> toReplaceKeyLow(Map map){
        if(map == null){
            return null;
        }
        Map<String,Object> result = new HashMap<>(map.size());
        for(Object key : map.keySet()){
            result.put(FieldUtils.underlineToCamel(key.toString()),map.get(key));
        }
        return result;
    }

    protected String createMapper(SqlSessionFactory sqlSessionFactory, String sql){
        //一般与namespace相同
        String mapperId = "Dynamic" + UUIDUtils.getUUID() + "Mapper";
        String contentXML = MapperUtils.generatorQueryList(sql,mapperId);
        Configuration mapperConfig = sqlSessionFactory.getConfiguration();
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(contentXML.getBytes(StandardCharsets.UTF_8));
            XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(inputStream, mapperConfig, UUIDUtils.getUUID(), mapperConfig.getSqlFragments());
            xmlMapperBuilder.parse();
            return mapperId;
        }catch (Exception e){
            logger.error("XML注册异常,mapperId={}",mapperId, e);
            throw e;
        }
    }

    //入参转码
    public abstract String transcoding(String param);
    public abstract List<TableInfo> buildTableInfo();
    public abstract void close();
    public abstract String getLimitSql(int page, int pageSize);


}
