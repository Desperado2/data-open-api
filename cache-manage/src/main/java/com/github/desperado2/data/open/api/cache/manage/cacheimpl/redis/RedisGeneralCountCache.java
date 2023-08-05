package com.github.desperado2.data.open.api.cache.manage.cacheimpl.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.desperado2.data.open.api.cache.manage.chche.IGeneralCountCache;
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
public class RedisGeneralCountCache extends AbstractRedisCache implements IGeneralCountCache {

    private final static Logger log = LoggerFactory.getLogger(RedisGeneralCountCache.class);

    public RedisGeneralCountCache(ObjectMapper objectMapper, RedisUtil redisUtil, OpenApiProperties openApiProperties) {
        super(objectMapper, redisUtil, openApiProperties);
    }


    @Override
    public void set(String cacheKey, Long value, Long time) {
        redisUtil.addValue(buildGeneralCacheKey(cacheKey), value, time);
    }

    @Override
    public void set(String cacheKey, Long value) {
        redisUtil.addValue(buildGeneralCacheKey(cacheKey), value);
    }

    @Override
    public Long get(String cacheKey) {
        return redisUtil.addValue(buildGeneralCacheKey(cacheKey), 0);
    }

    @Override
    public Long increaseAndGet(String cacheKey) {
        return redisUtil.increase(buildGeneralCacheKey(cacheKey));
    }

    @Override
    public Long addValueAndGet(String cacheKey, Long value) {
        return redisUtil.addValue(buildGeneralCacheKey(cacheKey), value);
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
