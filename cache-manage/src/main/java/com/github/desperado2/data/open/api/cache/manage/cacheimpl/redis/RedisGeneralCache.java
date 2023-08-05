package com.github.desperado2.data.open.api.cache.manage.cacheimpl.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.desperado2.data.open.api.cache.manage.chche.IGeneralCache;
import com.github.desperado2.data.open.api.common.manage.config.OpenApiProperties;
import com.github.desperado2.data.open.api.common.manage.config.RedisTemplateConfig;
import com.github.desperado2.data.open.api.common.manage.config.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 通用缓存实现
 * @author tu nan
 * @date 2023/4/4
 **/
@Component
@ConditionalOnBean(RedisTemplateConfig.class)
@ConditionalOnProperty(value = "open.data.platform.base.cache-type", havingValue = "redis")
public class RedisGeneralCache extends AbstractRedisCache implements IGeneralCache {

    private final static Logger log = LoggerFactory.getLogger(RedisGeneralCache.class);

    public RedisGeneralCache(ObjectMapper objectMapper, RedisUtil redisUtil, OpenApiProperties openApiProperties) {
        super(objectMapper, redisUtil, openApiProperties);
    }


    @Override
    public void set(String cacheKey, String value, Long time) {
        redisUtil.set(buildGeneralCacheKey(cacheKey), value, time);
    }

    @Override
    public void set(String cacheKey, String value) {
        redisUtil.set(buildGeneralCacheKey(cacheKey), value);
    }

    @Override
    public String get(String cacheKey) {
        return redisUtil.get(buildGeneralCacheKey(cacheKey));
    }

    @Override
    public void remove(String cacheKey) {
        redisUtil.remove(buildGeneralCacheKey(cacheKey));
    }

    @Override
    public void clear() {
        redisUtil.remove(getKeys());
    }

    @Override
    public boolean exists(String cacheKey) {
        return redisUtil.exists(buildGeneralCacheKey(cacheKey));
    }

    @Override
    public long getKeyTtlForSeconds(String cacheKey) {
       return redisUtil.getKeyTtlForSeconds(buildGeneralCacheKey(cacheKey));
    }

    @Override
    public void expire(String cacheKey, Long time) {
        redisUtil.expire(buildGeneralCacheKey(cacheKey), time);
    }

    @Override
    protected String buildPrefix() {
        return "open-api:general:";
    }

    private String buildGeneralCacheKey(String cacheKey) {
        return buildPrefix() + cacheKey;
    }
}
