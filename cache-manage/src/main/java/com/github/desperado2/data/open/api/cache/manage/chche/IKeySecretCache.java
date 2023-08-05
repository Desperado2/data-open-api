package com.github.desperado2.data.open.api.cache.manage.chche;

import com.github.desperado2.data.open.api.cache.manage.model.KeySecretInfo;

import java.util.Collection;

/**
 * 认证秘钥缓存
 *
 * @author tu nan
 * @date 2023/3/18
 **/
public interface IKeySecretCache {

    /**
     * 获取缓存
     * @param keySecretInfo 缓存信息
     * @return 缓存
     */
    KeySecretInfo get(KeySecretInfo keySecretInfo);

    /**
     * 新增缓存
     * @param keySecretInfo 缓存
     */
    void put(KeySecretInfo keySecretInfo);

    /**
     * 移除缓存
     * @param keySecretInfo 缓存key
     */
    void remove(KeySecretInfo keySecretInfo);

    /**
     * 移除所有缓存
     */
    void removeAll();

    /**
     * 获取所有缓存
     * @return 缓存
     */
    Collection<KeySecretInfo> getAll();
}
