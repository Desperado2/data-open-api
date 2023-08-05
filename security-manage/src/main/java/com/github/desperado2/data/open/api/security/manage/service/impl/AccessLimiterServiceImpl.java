package com.github.desperado2.data.open.api.security.manage.service.impl;


import com.github.desperado2.data.open.api.common.manage.config.RedisUtil;
import com.github.desperado2.data.open.api.security.manage.cache.LocalRequestLogCache;
import com.github.desperado2.data.open.api.security.manage.service.IAccessLimiterService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;

/**
 * 限流实现
 * @author tu nan
 * @date 2023/3/27
 **/
@ConditionalOnMissingBean(RedisUtil.class)
@ConditionalOnExpression("'${open.data.platform.base.access-limiter-type:redis}'.equals('local') && '${open.data.platform.base.access-limiter-open:false}'")
@Service
public class AccessLimiterServiceImpl implements IAccessLimiterService {

    private static final Logger log = LoggerFactory.getLogger(AccessLimiterServiceImpl.class);

    private final LocalRequestLogCache localRequestLogCache;

    public AccessLimiterServiceImpl(LocalRequestLogCache localRequestLogCache) {
        this.localRequestLogCache = localRequestLogCache;
    }

    @Override
    public boolean verify(String key, Integer limit, Integer timeScope) {
        if(StringUtils.isBlank(key)){
            return true;
        }
        return  localRequestLogCache.tryAcquire(key, limit, timeScope);
    }
}
