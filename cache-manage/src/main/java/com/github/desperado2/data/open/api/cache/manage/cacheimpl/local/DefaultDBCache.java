package com.github.desperado2.data.open.api.cache.manage.cacheimpl.local;


import com.github.desperado2.data.open.api.cache.manage.chche.IDBCache;
import com.github.desperado2.data.open.api.cache.manage.utils.LRUHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;


/**
 * 默认数据库查询缓存操作
 */
@Component
@ConditionalOnProperty(value = "open.data.platform.base.cache-type", havingValue = "local")
public class DefaultDBCache implements IDBCache {

    private final static Logger log = LoggerFactory.getLogger(DefaultDBCache.class);


    private final Integer maxCacheSize = 1024;

    // 定时器
    private final Timer timer = new Timer();

    //过期检测周期
    private static final long CHECK_TIME_SECOND = 10 * 1000;

    private final LRUHashMap<String,Object> cacheMap = new LRUHashMap<>(maxCacheSize);
    private final HashMap<String,Long> expiresTime = new HashMap<>();

    //过期处理
    private final TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            Set<String> keys = expiresTime.keySet();
            for (String key : keys){
                if (expiresTime.get(key) <= System.currentTimeMillis()){
                    synchronized (this){
                        expiresTime.remove(key);
                        cacheMap.remove(key);
                        log.debug("Automatic cache expiration:{}",key);
                    }
                }
            }
        }
    };


    @PostConstruct
    public void init(){
        timer.schedule(timerTask, CHECK_TIME_SECOND,CHECK_TIME_SECOND);
    }

    @Override
    public void set(String cacheKey, Object value, Long cacheTime) {
        synchronized (this){
            this.cacheMap.put(cacheKey,value);
            this.expiresTime.put(cacheKey,System.currentTimeMillis()+cacheTime * 1000);
        }
    }

    @Override
    public Object get(String cacheKey) {
        return this.cacheMap.get(cacheKey);
    }

    @Override
    public List<Map> getList(String cacheKey) {
        Object s = this.cacheMap.get(cacheKey);
        if(s == null){
            return null;
        }
        // 将JSON格式转换为列表
        return (List<Map>) s;
    }

    @Override
    public Map getMap(String cacheKey) {
        Object s = this.cacheMap.get(cacheKey);
        if(s == null){
            return null;
        }
        return (Map) s;
    }

    @Override
    public void remove(String cacheKey) {
        synchronized (this){
            this.cacheMap.remove(cacheKey);
            this.expiresTime.remove(cacheKey);
        }
    }

    @Override
    public void clear() {
        synchronized (this){
            this.cacheMap.clear();
            this.expiresTime.clear();
        }
    }
}
