package com.github.desperado2.data.open.api.cache.manage.chche;

import com.github.desperado2.data.open.api.cache.manage.model.ApiApplyInfo;

import java.util.Collection;

/**
 * 接口申请信息缓存
 *
 * @author tu nan
 * @date 2023/3/18
 **/
public interface IApiApplyInfoCache {


    /**
     * 获取缓存
     * @param apiInfo 缓存key
     * @return 缓存
     */
    ApiApplyInfo get(ApiApplyInfo apiInfo);

    /**
     * 添加缓存
     * @param apiInfo 缓存信息
     */
    void put(ApiApplyInfo apiInfo);

    /**
     * 移除缓存
     * @param apiInfo 缓存信息
     */
    void remove(ApiApplyInfo apiInfo);

    /**
     * 删除所有
     */
    void removeAll();

    /**
     * 获取所有
     * @return 缓存
     */
    Collection<ApiApplyInfo> getAll();
}
