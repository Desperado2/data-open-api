package com.github.desperado2.data.open.api.engine.manage.function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author tu nan
 * @date 2023/7/10
 **/
@Component
public class LockFunction {

    @Value("${open.data.platform.base.db-request-limit:1}")
    private Integer requestLimit;

    private final static Logger log = LoggerFactory.getLogger(LockFunction.class);

    private static final  String  LOCK = "LOCK";

    private Map<String, ArrayBlockingQueue<Long> > maps = new ConcurrentHashMap<>();

    public void put(String key, Long value) throws InterruptedException {
        synchronized (LockFunction.class){
            if(maps.containsKey(key)){
                maps.get(key).put(value);
            }else{
                maps.put(key, new ArrayBlockingQueue<Long>(requestLimit){{put(value);}});
            }
        }
    }

    public Long peek(String key) throws InterruptedException {
        synchronized (LOCK.intern()){
            if(maps.containsKey(key) && maps.get(key).size() > 0){
                Long take = maps.get(key).take();
                if(maps.get(key).size() == 0){
                    maps.remove(key);
                }
                return take;
            }else if(maps.containsKey(key) && maps.get(key).size() == 0){
                maps.remove(key);
            }
            return 0L;
        }
    }
}
