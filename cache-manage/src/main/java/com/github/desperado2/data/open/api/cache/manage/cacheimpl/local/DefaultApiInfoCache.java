package com.github.desperado2.data.open.api.cache.manage.cacheimpl.local;


import com.github.desperado2.data.open.api.cache.manage.chche.IApiInfoCache;
import com.github.desperado2.data.open.api.cache.manage.model.ApiInfo;
import com.github.desperado2.data.open.api.common.manage.config.OpenApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * API信息缓存
 */
@Component
@ConditionalOnProperty(value = "open.data.platform.base.cache-type", havingValue = "local")
public class DefaultApiInfoCache implements IApiInfoCache {

    private static final Logger logger = LoggerFactory.getLogger(DefaultApiInfoCache.class);

    private final Map<String, ApiInfo> cacheApiInfo = new ConcurrentHashMap<>();

    private final OpenApiProperties openApiProperties;

    public DefaultApiInfoCache(OpenApiProperties openApiProperties) {
        this.openApiProperties = openApiProperties;
    }


    @Override
    public ApiInfo get(ApiInfo apiInfo){
        logger.info("查询:{}", apiInfo.toString());
        ApiInfo apiInfo1 = cacheApiInfo.get(buildApiInfoKey(apiInfo));
        if(apiInfo1 == null){
            apiInfo.setPath(apiInfo.getPath().replaceFirst(openApiProperties.getBaseRegisterPath(), ""));
            apiInfo.setMethod(apiInfo.getMethod().toUpperCase(Locale.ROOT));
            apiInfo1 = cacheApiInfo.get(buildApiInfoKey(apiInfo));
        }
        logger.info("api:{}", apiInfo1);
        return apiInfo1;
    }

    @Override
    public Collection<ApiInfo> getAll() {
        return cacheApiInfo.values();
    }

    @Override
    public void removeAll() {
        cacheApiInfo.clear();
    }

    @Override
    public void remove(ApiInfo apiInfo) {
        cacheApiInfo.remove(buildApiInfoKey(apiInfo));
    }

    @Override
    public void put(ApiInfo apiInfo) {
        logger.info("新增API:{},{}", buildApiInfoKey(apiInfo),apiInfo.toString());
        cacheApiInfo.put(buildApiInfoKey(apiInfo),apiInfo);
    }

    private String buildApiInfoKey(ApiInfo apiInfo) {
        return apiInfo.getMethod().toUpperCase(Locale.ROOT) +"_"+ apiInfo.getPath();
    }

}
