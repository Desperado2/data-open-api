package com.github.desperado2.data.open.api.cache.manage.utils;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUHashMap<String,Object> extends LinkedHashMap {

    private Integer maxCacheSize = null;

    private LRUHashMap(){

    }

    public LRUHashMap(Integer maxCacheSize){
        this.maxCacheSize = maxCacheSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > maxCacheSize;
    }
}
