package com.github.desperado2.data.open.api.cache.manage.cacheimpl.redis;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.desperado2.data.open.api.cache.manage.chche.IDBCache;
import com.github.desperado2.data.open.api.common.manage.config.OpenApiProperties;
import com.github.desperado2.data.open.api.common.manage.config.RedisTemplateConfig;
import com.github.desperado2.data.open.api.common.manage.config.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/**
 * 默认数据库查询缓存操作
 */
@Component
@ConditionalOnBean(RedisTemplateConfig.class)
@ConditionalOnProperty(value = "open.data.platform.base.cache-type", havingValue = "redis")
public class RedisDBCache extends AbstractRedisCache implements IDBCache {

    private final static Logger log = LoggerFactory.getLogger(RedisDBCache.class);

    public RedisDBCache(ObjectMapper objectMapper, RedisUtil redisUtil, OpenApiProperties openApiProperties) {
        super(objectMapper, redisUtil, openApiProperties);
    }

    @Override
    public void set(String cacheKey, Object value, Long cacheTime) {
        try {
            String jsonString = JSON.toJSONString(value, SerializerFeature.WRITE_MAP_NULL_FEATURES, SerializerFeature.QuoteFieldNames);
            redisUtil.set(buildApiInfoKey(cacheKey), jsonString, cacheTime);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String get(String cacheKey) {
        return redisUtil.get(buildApiInfoKey(cacheKey));
    }

    @Override
    public List<Map> getList(String cacheKey){
        String s = get(cacheKey);
        if(s == null){
            return null;
        }
        return JSON.parseArray(s, Map.class);
    }

    @Override
    public Map getMap(String cacheKey) {
        String s = get(cacheKey);
        if(s == null){
            return null;
        }
        return JSON.parseObject(s, Map.class);
    }

    @Override
    public void remove(String cacheKey) {
        redisUtil.remove(buildApiInfoKey(cacheKey));
    }

    @Override
    public void clear() {
        redisUtil.remove(getKeys());
    }

    protected String buildPrefix(){
        return "open-api:db-cache";
    }

    private String buildApiInfoKey(String cacheKey) {
        return buildPrefix() +":"+ cacheKey;
    }
}
