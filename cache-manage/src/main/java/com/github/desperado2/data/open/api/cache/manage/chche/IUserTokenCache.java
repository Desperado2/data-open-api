package com.github.desperado2.data.open.api.cache.manage.chche;

import com.github.desperado2.data.open.api.common.manage.model.BaseUserModel;

/**
 * 用户登录token缓存操作
 */
public interface IUserTokenCache {

    /**
     * 新增缓存
     * @param cacheKey key
     * @param value 值
     * @param time 过期时间  秒
     */
    void set(String cacheKey, BaseUserModel value, Long time);

    /**
     * 获取缓存
     * @param cacheKey key
     * @return 过期时间
     */
    BaseUserModel get(String cacheKey);

    /**
     * 删除缓存
     * @param cacheKey 缓存key
     */
    void remove(String cacheKey);

    /**
     * 清理所有缓存
     */
    void clear();

    /**
     * 判断缓存是否存在
     * @param cacheKey 缓存key
     * @return true存在
     */
    boolean exists(String cacheKey);

    /**
     * 获取缓存剩余的有效期，秒
     * @param cacheKey 缓存key
     * @return 剩余时间，秒
     */
    long getKeyTtlForSeconds(String cacheKey);

    /**
     * 续期
     * @param cacheKey key
     * @param time 续期时长 ，秒
     */
    void expire(String cacheKey,Long time);
}
