package com.github.desperado2.data.open.api.cache.manage.chche;


import com.github.desperado2.data.open.api.cache.manage.model.ApiLogsCache;

import java.util.Collection;

/**
 * 日志缓存接口
 * @author tu nan
 * @date 2023/3/18
 **/
public interface ILogCache {

    /**
     * 获取缓存
     * @param apiLogsCache 缓存
     * @return 缓存信息
     */
    ApiLogsCache get(ApiLogsCache apiLogsCache);

    /**
     * 添加缓存
     * @param apiLogsCache 缓存
     */
    void put(ApiLogsCache apiLogsCache);

    /**
     * 移除缓存
     * @param apiLogsCache 缓存key
     */
    void remove(ApiLogsCache apiLogsCache);

    /**
     * 移除所有缓存
     */
    void removeAll();

    /**
     * 获取所有缓存
     * @return 缓存
     */
    Collection<ApiLogsCache> getAll();
}
