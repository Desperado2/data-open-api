package com.github.desperado2.data.open.api.security.manage.service.impl;


import com.github.desperado2.data.open.api.common.manage.config.RedisUtil;
import com.github.desperado2.data.open.api.security.manage.service.IAccessLimiterService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Redis实现
 * @author tu nan
 * @date 2023/3/27
 **/
@ConditionalOnBean(RedisUtil.class)
@ConditionalOnExpression("'${open.data.platform.base.access-limiter-type:redis}'.equals('redis') && '${open.data.platform.base.access-limiter-open:false}'")
@Service
public class RedisAccessLimiterServiceImpl implements IAccessLimiterService {

    private static final Logger log = LoggerFactory.getLogger(RedisAccessLimiterServiceImpl.class);

    private final RedisUtil redisUtil;

    private final DefaultRedisScript<Long> defaultRedisScript;

    public RedisAccessLimiterServiceImpl(RedisUtil redisUtil, DefaultRedisScript<Long> defaultRedisScript) {
        this.redisUtil = redisUtil;
        this.defaultRedisScript = defaultRedisScript;
    }

    @Override
    public boolean verify(String key, Integer limit, Integer timeScope) {
        if(StringUtils.isBlank(key)){
            return true;
        }
        Long requestLimit = 1L;
        String tag = "open-api";
        try {
            requestLimit = redisUtil.executeLong(defaultRedisScript, Arrays.asList(key,tag,String.valueOf(limit),String.valueOf(timeScope)));
        }catch (Exception e){
            log.warn("redis访问异常，允许业务继续执行");
        }
        return requestLimit != 0;
    }
}
