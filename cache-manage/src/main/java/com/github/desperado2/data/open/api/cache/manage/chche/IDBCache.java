package com.github.desperado2.data.open.api.cache.manage.chche;

import java.util.List;
import java.util.Map;

/**
 * 数据库缓存操作
 */
public interface IDBCache {

    /**
     * 添加缓存
     * @param cacheKey 缓存key
     * @param value 值
     * @param cacheTime 缓存超时时间
     */
    void set(String cacheKey,Object value,Long cacheTime);

    /**
     * 获取缓存
     * @param cacheKey 缓存key
     * @return 缓存
     */
    Object get(String cacheKey);


    List<Map> getList(String cacheKey);

    Map getMap(String cacheKey);

    /**
     * 移除缓存
     * @param cacheKey 缓存key
     */
    void remove(String cacheKey);

    /**
     * 清理所有缓存
     */
    void clear();
}
