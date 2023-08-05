package com.github.desperado2.data.open.api.cache.manage.cacheimpl.redis;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.desperado2.data.open.api.cache.manage.chche.IApiApplyInfoCache;
import com.github.desperado2.data.open.api.cache.manage.model.ApiApplyInfo;
import com.github.desperado2.data.open.api.common.manage.config.OpenApiProperties;
import com.github.desperado2.data.open.api.common.manage.config.RedisTemplateConfig;
import com.github.desperado2.data.open.api.common.manage.config.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class RedisApiApplyInfoCache extends AbstractRedisCache implements IApiApplyInfoCache {

    private static final Logger logger = LoggerFactory.getLogger(RedisApiApplyInfoCache.class);

    public RedisApiApplyInfoCache(ObjectMapper objectMapper, RedisUtil redisUtil, OpenApiProperties openApiProperties) {
        super(objectMapper, redisUtil, openApiProperties);
    }

    @Override
    public ApiApplyInfo get(ApiApplyInfo apiApplyInfo){
        String strValue = redisUtil.get(buildApiApplyInfoKey(apiApplyInfo));
        try {
            if(StringUtils.isNotBlank(strValue)){
                return objectMapper.readValue(strValue,ApiApplyInfo.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Collection<ApiApplyInfo> getAll() {
        return Objects.requireNonNull(redisUtil.multiGet(getKeys())).stream().map(item->{
            try {
                return objectMapper.readValue(item,ApiApplyInfo.class);
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
    public void remove(ApiApplyInfo apiApplyInfo) {
        redisUtil.remove(buildApiApplyInfoKey(apiApplyInfo));
    }

    @Override
    public void put(ApiApplyInfo apiApplyInfo) {
       try {
           String strValue = objectMapper.writeValueAsString(apiApplyInfo);
           redisUtil.set(buildApiApplyInfoKey(apiApplyInfo),strValue);
       }catch (Exception e){
           e.printStackTrace();
       }
    }

    protected String buildPrefix(){
        return "open-api:apply:";
    }

    private String buildApiApplyInfoKey(ApiApplyInfo apiApplyInfo) {
        logger.info("查询{}",apiApplyInfo.getUserId() +"_"+ apiApplyInfo.getApiId());
        return buildPrefix() + apiApplyInfo.getUserId() +"_"+ apiApplyInfo.getApiId();
    }
}
