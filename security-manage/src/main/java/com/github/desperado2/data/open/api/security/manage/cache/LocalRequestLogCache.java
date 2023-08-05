package com.github.desperado2.data.open.api.security.manage.cache;


import com.github.desperado2.data.open.api.common.manage.config.RedisUtil;
import com.google.common.util.concurrent.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


/**
 * 本地请求记录缓存
 */
@ConditionalOnMissingBean(RedisUtil.class)
@ConditionalOnExpression("'${open.data.platform.base.access-limiter-type:redis}'.equals('local') && '${open.data.platform.base.access-limiter-open:false}'")
@Component
public class LocalRequestLogCache{

    private final static Logger log = LoggerFactory.getLogger(LocalRequestLogCache.class);

    // 构造令牌桶
    Map<String, RateLimiter> rateLimiterMap = new ConcurrentHashMap<>();


    /**
     * 判断是否限流
     * @param key 唯一标识
     * @param limit 限速量
     * @param timeScope 限速时间范围
     * @return true通过  false 不通过
     */
    public boolean tryAcquire(String key,  Integer limit, Integer timeScope){
        synchronized (key){
            if(rateLimiterMap.containsKey(key)){
                return rateLimiterMap.get(key).tryAcquire();
            }else{
                rateLimiterMap.put(key, RateLimiter.create(limit/(timeScope*1.0), 1, TimeUnit.SECONDS)) ;
                return true;
            }
        }
    }
}
