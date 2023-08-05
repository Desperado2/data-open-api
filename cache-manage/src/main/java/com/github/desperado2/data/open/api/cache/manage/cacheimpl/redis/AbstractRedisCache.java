package com.github.desperado2.data.open.api.cache.manage.cacheimpl.redis;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.desperado2.data.open.api.common.manage.config.OpenApiProperties;
import com.github.desperado2.data.open.api.common.manage.config.RedisTemplateConfig;
import com.github.desperado2.data.open.api.common.manage.config.RedisUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 抽象缓存结构
 * @author tu nan
 * @date 2023/4/4
 **/
@Component
@ConditionalOnBean(RedisTemplateConfig.class)
@ConditionalOnProperty(value = "open.data.platform.base.cache-type", havingValue = "redis")
public abstract class AbstractRedisCache {

    protected final ObjectMapper objectMapper;

    protected final RedisUtil redisUtil;

    protected final OpenApiProperties openApiProperties;

    public AbstractRedisCache(ObjectMapper objectMapper, RedisUtil redisUtil, OpenApiProperties openApiProperties) {
        this.objectMapper = objectMapper;
        this.redisUtil = redisUtil;
        this.openApiProperties = openApiProperties;
    }

    protected abstract String buildPrefix();


    protected List<String> getKeys(){
        String patternKey = buildPrefix()+":*";
        return redisUtil.getKeys(patternKey);
    }
}
