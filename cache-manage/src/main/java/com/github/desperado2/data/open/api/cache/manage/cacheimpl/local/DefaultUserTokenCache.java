package com.github.desperado2.data.open.api.cache.manage.cacheimpl.local;


import com.github.desperado2.data.open.api.cache.manage.chche.IUserTokenCache;
import com.github.desperado2.data.open.api.cache.manage.utils.LRUHashMap;
import com.github.desperado2.data.open.api.common.manage.model.BaseUserModel;
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
 * 默认用户登录token实现
 * @author tu nan
 * @date 2023/4/4
 **/
@Component
@ConditionalOnProperty(value = "open.data.platform.base.cache-type", havingValue = "local")
public class DefaultUserTokenCache implements IUserTokenCache {

    private final static Logger log = LoggerFactory.getLogger(DefaultDBCache.class);


    private Integer maxCacheSize = 1024;

    // 定时器
    private Timer timer = new Timer();

    //过期检测周期
    private static final long CHECK_TIME_SECOND = 10 * 1000;

    private LRUHashMap<String,BaseUserModel> cacheMap = new LRUHashMap<>(maxCacheSize);
    private HashMap<String,Long> expiresTime = new HashMap<>();

    //过期处理
    private TimerTask timerTask = new TimerTask() {
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
    public void set(String cacheKey, BaseUserModel value, Long cacheTime) {
        synchronized (this){
            this.cacheMap.put(cacheKey,value);
            this.expiresTime.put(cacheKey,System.currentTimeMillis()+cacheTime * 1000);
        }
    }

    @Override
    public BaseUserModel get(String cacheKey) {
        return (BaseUserModel) this.cacheMap.get(cacheKey);
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
        return (this.expiresTime.get(cacheKey) -  System.currentTimeMillis())/1000;
    }

    @Override
    public void expire(String cacheKey, Long time) {
        synchronized (this){
            if(this.expiresTime.containsKey(cacheKey)){
                this.expiresTime.put(cacheKey, System.currentTimeMillis() + time * 1000);
            }
        }
    }
}
