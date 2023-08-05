package com.github.desperado2.data.open.api.cache.manage.chche;


import com.github.desperado2.data.open.api.cache.manage.model.ApiInfo;

import java.util.Collection;

/**
 * API信息缓存
 */
public interface IApiInfoCache {

    /**
     * 获取API缓存
     * @param apiInfo 缓存key
     * @return 缓存
     */
    ApiInfo get(ApiInfo apiInfo);

    /**
     * 添加缓存
     * @param apiInfo 缓存信息
     */
    void put(ApiInfo apiInfo);

    /**
     * 删除缓存
     * @param apiInfo 缓存信息
     */
    void remove(ApiInfo apiInfo);

    /**
     * 删除所有缓存
     */
    void removeAll();

    /**
     * 获取所有缓存
     * @return 所有缓存
     */
    Collection<ApiInfo> getAll();
}
