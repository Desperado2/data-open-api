package com.github.desperado2.data.open.api.cache.manage.cacheimpl.local;


import com.github.desperado2.data.open.api.cache.manage.chche.ILogCache;
import com.github.desperado2.data.open.api.cache.manage.model.ApiLogsCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认日志缓存实现
 * @author tu nan
 * @date 2023/3/18
 **/
@Component
public class DefaultLogCache implements ILogCache {

    private final static Logger log = LoggerFactory.getLogger(DefaultDBCache.class);

    private final Map<String, ApiLogsCache> apiLogCache = new ConcurrentHashMap<>();

    // 定时器
    private final Timer timer = new Timer();

    //过期检测周期
    private static final long CHECK_TIME_SECOND = 10 * 1000;

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
                        apiLogCache.remove(key);
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
    public ApiLogsCache get(ApiLogsCache apiLogsCache) {
        return apiLogCache.get(buildKey(apiLogsCache));
    }

    @Override
    public void put(ApiLogsCache apiLogsCache) {
        synchronized (this){
            apiLogCache.put(buildKey(apiLogsCache), apiLogsCache);
            // 默认的日志过期时间
            long defaultExpire = 3 * 60 * 1000L;
            expiresTime.put(buildKey(apiLogsCache), System.currentTimeMillis() + defaultExpire);
        }
    }

    @Override
    public ApiLogsCache remove(ApiLogsCache apiLogsCache) {
        synchronized (this){
            expiresTime.remove(buildKey(apiLogsCache));
            return apiLogCache.remove(buildKey(apiLogsCache));
        }
    }

    @Override
    public void removeAll() {
        synchronized (this){
            apiLogCache.clear();
            expiresTime.clear();
        }
    }

    @Override
    public Collection<ApiLogsCache> getAll() {
        return apiLogCache.values();
    }

    private String buildKey(ApiLogsCache apiLogsCache){
        return apiLogsCache.getLogKey();
    }
}
