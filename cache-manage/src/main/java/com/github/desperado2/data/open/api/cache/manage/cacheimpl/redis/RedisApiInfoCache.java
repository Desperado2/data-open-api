package com.github.desperado2.data.open.api.cache.manage.cacheimpl.redis;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.desperado2.data.open.api.cache.manage.chche.IApiInfoCache;
import com.github.desperado2.data.open.api.cache.manage.model.ApiInfo;
import com.github.desperado2.data.open.api.common.manage.config.OpenApiProperties;
import com.github.desperado2.data.open.api.common.manage.config.RedisTemplateConfig;
import com.github.desperado2.data.open.api.common.manage.config.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 集群通知
 */
@Component
@ConditionalOnBean(RedisTemplateConfig.class)
@ConditionalOnProperty(value = "open.data.platform.base.cache-type", havingValue = "redis")
public class RedisApiInfoCache extends AbstractRedisCache  implements IApiInfoCache {

    public RedisApiInfoCache(ObjectMapper objectMapper, RedisUtil redisUtil, OpenApiProperties openApiProperties) {
        super(objectMapper, redisUtil, openApiProperties);
    }


    @Override
    public ApiInfo get(ApiInfo apiInfo) {
        ApiInfo newApiInfo = null;
        String strValue = redisUtil.get(buildApiInfoKey(apiInfo));
        if(StringUtils.isBlank(strValue)){
            apiInfo.setPath(apiInfo.getPath().replaceFirst(openApiProperties.getBaseRegisterPath(), ""));
            apiInfo.setMethod(apiInfo.getMethod().toUpperCase(Locale.ROOT));
            strValue = redisUtil.get(buildApiInfoKey(apiInfo));
        }
        try {
            newApiInfo =  objectMapper.readValue(strValue,ApiInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newApiInfo;
    }

    @Override
    public void put(ApiInfo apiInfo) {
        try {
            String strValue = objectMapper.writeValueAsString(apiInfo);
            redisUtil.set(buildApiInfoKey(apiInfo),strValue);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(ApiInfo apiInfo) {
        redisUtil.remove(buildApiInfoKey(apiInfo));
    }

    @Override
    public void removeAll() {
        redisUtil.remove(getKeys());
    }

    @Override
    public Collection<ApiInfo> getAll() {
        return Objects.requireNonNull(redisUtil.multiGet(getKeys())).stream().map(item->{
            try {
                return objectMapper.readValue(item,ApiInfo.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
    }


    protected String buildPrefix(){
        return "open-api:api-info:";
    }

    private String buildApiInfoKey(ApiInfo apiInfo) {
        return buildPrefix()+":"+apiInfo.getMethod().toUpperCase(Locale.ROOT) +"-"+ apiInfo.getPath();
    }
}
