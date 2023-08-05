package com.github.desperado2.data.open.api.cache.manage.cacheimpl.local;


import com.github.desperado2.data.open.api.cache.manage.chche.IKeySecretCache;
import com.github.desperado2.data.open.api.cache.manage.model.KeySecretInfo;
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
public class DefaultKeySecretInfoCache implements IKeySecretCache {

    private Map<String, KeySecretInfo> cacheKeySecretInfo = new ConcurrentHashMap<>();


    @Override
    public KeySecretInfo get(KeySecretInfo keySecretInfo){
        return cacheKeySecretInfo.get(buildKeySecretInfoKey(keySecretInfo));
    }

    @Override
    public Collection<KeySecretInfo> getAll() {
        return cacheKeySecretInfo.values();
    }

    @Override
    public void removeAll() {
        cacheKeySecretInfo.clear();
    }

    @Override
    public void remove(KeySecretInfo keySecretInfo) {
        cacheKeySecretInfo.remove(buildKeySecretInfoKey(keySecretInfo));
    }

    @Override
    public void put(KeySecretInfo keySecretInfo) {
        cacheKeySecretInfo.put(buildKeySecretInfoKey(keySecretInfo),keySecretInfo);
    }

    private String buildKeySecretInfoKey(KeySecretInfo apiInfo) {
        return apiInfo.getAppKey();
    }

}
