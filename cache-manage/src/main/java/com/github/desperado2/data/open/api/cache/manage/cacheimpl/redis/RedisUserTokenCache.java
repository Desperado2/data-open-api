package com.github.desperado2.data.open.api.cache.manage.cacheimpl.redis;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.desperado2.data.open.api.cache.manage.chche.IUserTokenCache;
import com.github.desperado2.data.open.api.common.manage.config.OpenApiProperties;
import com.github.desperado2.data.open.api.common.manage.config.RedisTemplateConfig;
import com.github.desperado2.data.open.api.common.manage.config.RedisUtil;
import com.github.desperado2.data.open.api.common.manage.model.BaseUserModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 默认用户登录token实现
 * @author tu nan
 * @date 2023/4/4
 **/
@Component
@ConditionalOnBean(RedisTemplateConfig.class)
@ConditionalOnProperty(value = "open.data.platform.base.cache-type", havingValue = "redis")
public class RedisUserTokenCache extends AbstractRedisCache implements IUserTokenCache {


    public RedisUserTokenCache(ObjectMapper objectMapper, RedisUtil redisUtil, OpenApiProperties openApiProperties) {
        super(objectMapper, redisUtil, openApiProperties);
    }


    @Override
    public void set(String cacheKey, BaseUserModel value, Long cacheTime) {
        try {
            String strValue = objectMapper.writeValueAsString(value);
            redisUtil.set(buildUserTokenKey(cacheKey), strValue, cacheTime);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public BaseUserModel get(String cacheKey) {
        String strValue = redisUtil.get(buildUserTokenKey(cacheKey));
        try {
            if(StringUtils.isNotBlank(strValue)){
                return objectMapper.readValue(strValue, BaseUserModel.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void remove(String cacheKey) {
        redisUtil.remove(buildUserTokenKey(cacheKey));
    }

    @Override
    public void clear() {
        redisUtil.remove(getKeys());
    }

    @Override
    public boolean exists(String cacheKey) {
        return redisUtil.exists(buildUserTokenKey(cacheKey));
    }

    @Override
    public long getKeyTtlForSeconds(String cacheKey) {
        return redisUtil.getKeyTtlForSeconds(buildUserTokenKey(cacheKey));
    }

    @Override
    public void expire(String cacheKey, Long time) {
        redisUtil.expire(buildUserTokenKey(cacheKey), time);
    }

    protected String buildPrefix(){
        return "open-api:user-token:";
    }
    private String buildUserTokenKey(String cacheKey) {
        return buildPrefix() + cacheKey;
    }
}
