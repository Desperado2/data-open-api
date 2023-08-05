package com.github.desperado2.data.open.api.cache.manage.cacheimpl.local;


import com.github.desperado2.data.open.api.cache.manage.chche.IApiApplyInfoCache;
import com.github.desperado2.data.open.api.cache.manage.model.ApiApplyInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * API信息缓存
 */
@Component
@ConditionalOnProperty(value = "open.data.platform.base.cache-type", havingValue = "local")
public class DefaultApiApplyInfoCache implements IApiApplyInfoCache {

    private static final Logger logger = LoggerFactory.getLogger(DefaultApiApplyInfoCache.class);

    private final Map<String, ApiApplyInfo> cacheApiApplyInfo = new ConcurrentHashMap<>();

    @Override
    public ApiApplyInfo get(ApiApplyInfo apiApplyInfo){
        return cacheApiApplyInfo.get(buildApiApplyInfoKey(apiApplyInfo));
    }

    @Override
    public Collection<ApiApplyInfo> getAll() {
        return cacheApiApplyInfo.values();
    }

    @Override
    public void removeAll() {
        cacheApiApplyInfo.clear();
    }

    @Override
    public void remove(ApiApplyInfo apiApplyInfo) {
        cacheApiApplyInfo.remove(buildApiApplyInfoKey(apiApplyInfo));
    }

    @Override
    public void put(ApiApplyInfo apiApplyInfo) {
        cacheApiApplyInfo.put(buildApiApplyInfoKey(apiApplyInfo),apiApplyInfo);
    }

    private String buildApiApplyInfoKey(ApiApplyInfo apiApplyInfo) {
        logger.info("查询{}",apiApplyInfo.getUserId() +"_"+ apiApplyInfo.getApiId());
        return apiApplyInfo.getUserId() +"_"+ apiApplyInfo.getApiId();
    }

}
