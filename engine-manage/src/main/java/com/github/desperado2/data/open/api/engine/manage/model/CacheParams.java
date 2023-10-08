package com.github.desperado2.data.open.api.engine.manage.model;


/**
 * 缓存的配置
 * @author mujingjing
 * @date 2023/9/26
 **/
public class CacheParams {

    /**
     * 是否开启
     */
    private Boolean enableCache;

    /**
     * 缓存时长
     */
    private Long cacheValidity;

    public CacheParams() {
    }

    public CacheParams(Boolean enableCache, Long cacheValidity) {
        this.enableCache = enableCache;
        this.cacheValidity = cacheValidity;
    }

    public Boolean getEnableCache() {
        return enableCache;
    }

    public void setEnableCache(Boolean enableCache) {
        this.enableCache = enableCache;
    }

    public Long getCacheValidity() {
        return cacheValidity;
    }

    public void setCacheValidity(Long cacheValidity) {
        this.cacheValidity = cacheValidity;
    }
}
