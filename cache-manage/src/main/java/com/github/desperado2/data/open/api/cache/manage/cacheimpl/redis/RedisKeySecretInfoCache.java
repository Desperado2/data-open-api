package com.github.desperado2.data.open.api.cache.manage.cacheimpl.redis;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.desperado2.data.open.api.cache.manage.chche.IKeySecretCache;
import com.github.desperado2.data.open.api.cache.manage.model.KeySecretInfo;
import com.github.desperado2.data.open.api.common.manage.config.OpenApiProperties;
import com.github.desperado2.data.open.api.common.manage.config.RedisTemplateConfig;
import com.github.desperado2.data.open.api.common.manage.config.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * API信息缓存
 */
@Component
@ConditionalOnBean(RedisTemplateConfig.class)
@ConditionalOnProperty(value = "open.data.platform.base.cache-type", havingValue = "redis")
public class RedisKeySecretInfoCache extends AbstractRedisCache implements IKeySecretCache {

    public RedisKeySecretInfoCache(ObjectMapper objectMapper, RedisUtil redisUtil, OpenApiProperties openApiProperties) {
        super(objectMapper, redisUtil, openApiProperties);
    }

    @Override
    public KeySecretInfo get(KeySecretInfo keySecretInfo){
        String strValue = redisUtil.get(buildKeySecretInfoKey(keySecretInfo));
        try {
            if(StringUtils.isNotBlank(strValue)){
                return objectMapper.readValue(strValue, KeySecretInfo.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Collection<KeySecretInfo> getAll() {
        return Objects.requireNonNull(redisUtil.multiGet(getKeys())).stream().map(item->{
            try {
                return objectMapper.readValue(item,KeySecretInfo.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
    }

    @Override
    public void removeAll() {
        redisUtil.remove(getKeys());
    }

    @Override
    public void remove(KeySecretInfo keySecretInfo) {
        redisUtil.remove(buildKeySecretInfoKey(keySecretInfo));
    }

    @Override
    public void put(KeySecretInfo keySecretInfo) {
        try {
            String strValue = objectMapper.writeValueAsString(keySecretInfo);
            redisUtil.set(buildKeySecretInfoKey(keySecretInfo),strValue);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


    protected String buildPrefix(){
        return "open-api:key-secret:";
    }
    private String buildKeySecretInfoKey(KeySecretInfo apiInfo) {
        return buildPrefix() + apiInfo.getAppKey();
    }

}
