package com.github.desperado2.data.open.api.cache.manage.cacheimpl.local;

import com.github.desperado2.data.open.api.cache.manage.chche.IGeneralCountCache;
import com.github.desperado2.data.open.api.cache.manage.utils.LRUHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 通用缓存实现
 * @author tu nan
 * @date 2023/4/4
 **/
@Component
@ConditionalOnProperty(value = "open.data.platform.base.cache-type", havingValue = "local")
public class DefaultGeneralCountCache implements IGeneralCountCache {

    private final static Logger log = LoggerFactory.getLogger(DefaultGeneralCountCache.class);


    private final Integer maxCacheSize = 1024;

    // 定时器
    private final Timer timer = new Timer();

    //过期检测周期
    private static final long CHECK_TIME_SECOND = 10 * 1000;

    private final LRUHashMap<String,Long> cacheMap = new LRUHashMap<>(maxCacheSize);
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
    public void set(String cacheKey, Long value, Long time) {
        synchronized (this){
            this.cacheMap.put(cacheKey, value);
            this.expiresTime.put(cacheKey, System.currentTimeMillis() + time * 1000);
        }
    }

    @Override
    public void set(String cacheKey, Long value) {
        this.cacheMap.put(cacheKey, value);
    }

    @Override
    public Long get(String cacheKey) {
        return (Long) this.cacheMap.get(cacheKey);
    }

    @Override
    public Long increaseAndGet(String cacheKey) {
        synchronized (this){
            this.cacheMap.put(cacheKey, ((Long) this.cacheMap.get(cacheKey)) + 1);
            return ((Long) this.cacheMap.get(cacheKey)) + 1;
        }
    }

    @Override
    public Long addValueAndGet(String cacheKey, Long value) {
        synchronized (this){
            this.cacheMap.put(cacheKey, ((Long) this.cacheMap.get(cacheKey)) + value);
            return ((Long) this.cacheMap.get(cacheKey)) + value;
        }
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

    @Override
    public boolean exists(String cacheKey) {
        return this.cacheMap.containsKey(cacheKey);
    }

    @Override
    public long getKeyTtlForSeconds(String cacheKey) {
        synchronized (this){
            if(this.expiresTime.containsKey(cacheKey)){
                return (this.expiresTime.get(cacheKey) - System.currentTimeMillis()) / 1000;
            }
        }
       return -1;
    }

    @Override
    public void expire(String cacheKey, Long time) {
        synchronized (this){
            if(this.cacheMap.containsKey(cacheKey)){
                this.expiresTime.put(cacheKey, System.currentTimeMillis() + time * 1000);
            }
        }
    }
}
