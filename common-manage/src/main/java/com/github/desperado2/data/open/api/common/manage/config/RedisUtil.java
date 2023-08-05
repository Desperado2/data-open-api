package com.github.desperado2.data.open.api.common.manage.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 * @author tu nan
 * @date 2023/4/4
 **/
@Component("redisUtil")
@ConditionalOnProperty("spring.redis.host")
public class RedisUtil {

    private static final Logger log = LoggerFactory.getLogger(RedisUtil.class);

    private final StringRedisTemplate stringRedisTemplate;

    private final RedisTemplate<String,Object> redisTemplate;

    public RedisUtil(@Qualifier("stringRedisTemplate") StringRedisTemplate stringRedisTemplate, @Qualifier("redisTemplate") RedisTemplate redisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 写入key value
     *
     * @param key key
     * @param value 值
     * @return 成功true
     */
    public boolean set(final String key, String value) {
        boolean result = false;
        try {
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入key value设置过期时间,单位秒
     *
     * @param key key
     * @param value 值
     * @param expireTime 有效时长 秒
     * @return 成功true
     */
    public boolean set(final String key, String value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(key, value);
            stringRedisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 批量删除对应的value
     *
     * @param keys key集合
     */
    public void remove(final String... keys) {
        Arrays.stream(keys).forEach(this::remove);
    }

    /**
     * 批量删除对应的value
     *
     * @param keys key集合
     */
    public void remove(final List<String> keys) {
        keys.forEach(this::remove);
    }

    /**
     * 根据表达式批量删除key
     *
     * @param pattern 表达式
     */
    public void removePattern(final String pattern) {
        Set<String> keys = stringRedisTemplate.keys(pattern);
        if (keys != null && keys.size() > 0) {
            stringRedisTemplate.delete(keys);
        }
    }

    /**
     * 删除对应的value
     *
     * @param key key
     */
    public boolean remove(final String key) {
        if (exists(key)) {
            return Boolean.TRUE.equals(stringRedisTemplate.delete(key));
        }
        return true;
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key key
     * @return 存在 true
     */
    public boolean exists(final String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }

    /**
     * 读取缓存
     *
     * @param keyList key列表
     * @return 缓存值
     */
    public List<String> multiGet(final List<String> keyList) {
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        return operations.multiGet(keyList);
    }


    /**
     * 读取缓存
     *
     * @param key key
     * @return 缓存值
     */
    public String get(final String key) {
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        return operations.get(key);
    }

    /**
     * 添加hash数据
     *
     * @param key key
     * @param hashKey hashKey
     * @param value 值
     */
    public void hmSet(String key, Object hashKey, Object value) {
        HashOperations<String, Object, Object> hash = stringRedisTemplate.opsForHash();
        hash.put(key, hashKey, value);
    }

    /**
     * 读取hash数据
     *
     * @param key key
     * @param hashKey hashKey
     * @return 值
     */
    public Object hmGet(String key, Object hashKey) {
        HashOperations<String, Object, Object> hash = stringRedisTemplate.opsForHash();
        return hash.get(key, hashKey);
    }

    /**
     * 判断key和hashkey是否存在
     *
     * @param key key
     * @param hashKey hashKEY
     * @return 存在 true
     */
    public boolean isHmKey(String key, Object hashKey) {
        HashOperations<String, Object, Object> hash = stringRedisTemplate.opsForHash();
        return hash.hasKey(key, hashKey);
    }

   /**
    * 删除key和hashkey
    *
    * @param key key
    * @param hashKey hashKEY
    * @return 删除数量
    */
    public Long deleteHmKey(String key, Object... hashKey) {
        HashOperations<String, Object, Object> hash = stringRedisTemplate.opsForHash();
        return hash.delete(key, hashKey);
    }

    /**
     *  获取所有的hashkey的value值
     *
     * @param key key
     * @return 值
     */
    public List<Object> getHashValues(String key) {
        HashOperations<String, Object, Object> hash = stringRedisTemplate.opsForHash();
        return hash.values(key);
    }


   /**
    * 获取对应Hashkey值的所有值
    *
    * @param key key
    * @return 值
    */
    public Set<Object> getHashKeys(String key) {
        HashOperations<String, Object, Object> hash = stringRedisTemplate.opsForHash();
        return hash.keys(key);
    }

    
    /**
     * 自增1并获取
     *
     * @param key key
     * @return 自增1之后的值
     */
    public long increase(String key) {
        RedisAtomicLong counter = new RedisAtomicLong(key, Objects.requireNonNull(stringRedisTemplate.getConnectionFactory()));
        return counter.incrementAndGet();
    }

    /**
     * 自增1，并获取，并且设置过期时间
     *
     * @param key key
     * @param expireTime 过期时长  秒
     * @return 自增1之后的值
     */
    public long increase(String key, long expireTime) {
        RedisAtomicLong counter = new RedisAtomicLong(key, Objects.requireNonNull(stringRedisTemplate.getConnectionFactory()));
        counter.expire(expireTime, TimeUnit.SECONDS);
        return counter.incrementAndGet();
    }
    
   /**
    * 加值并获取
    *
    * @param key key
    * @param increment 自增的值
    * @return 相加之后的值
    */
    public long addValue(String key, long increment) {
        RedisAtomicLong counter = new RedisAtomicLong(key, Objects.requireNonNull(stringRedisTemplate.getConnectionFactory()));
        return counter.addAndGet(increment);
    }


    /**
     * 加值并获取 并设置过期时间
     *
     * @param key key
     * @param increment 增长的值
     * @param timeOut 有效期 秒
     * @return 相加之后的值
     */
    public long addValue(String key, long increment, long timeOut) {
        RedisAtomicLong counter = new RedisAtomicLong(key, Objects.requireNonNull(stringRedisTemplate.getConnectionFactory()));
        counter.expire(timeOut, TimeUnit.SECONDS);
        return counter.addAndGet(increment);
    }


    /**
     * SETNX key value
     * 将 key 的值设为 value ，当且仅当 key 不存在。
     * 若给定的 key 已经存在，则 SETNX 不做任何动作。
     *
     * @param key key
     * @param value 值
     * @param timeOut 有效时长 秒
     * @return 是否成功
     */
    public boolean setNx(final String key, final String value,final long timeOut) {
        Object obj = null;
        try {
            obj = stringRedisTemplate.execute((RedisCallback<Object>) connection -> {
                StringRedisSerializer serializer = new StringRedisSerializer();
                Boolean success = connection.setNX(Objects.requireNonNull(serializer.serialize(key)),
                        Objects.requireNonNull(serializer.serialize(value)));
                connection.expire(Objects.requireNonNull(serializer.serialize(key)), timeOut);
                connection.close();
                return success;
            });
        } catch (Exception e) {//异常获取失败
            log.error("setNX redis error, key : {}" + key);
        }
        return obj != null ? (Boolean) obj : false;
    }

    /**
     * 执行脚本
     * @param redisScript lua脚本
     * @param keys key
     * @param args 值
     */
    public void execute(RedisScript<Boolean> redisScript, List<String> keys, String... args){
        stringRedisTemplate.execute(redisScript, keys, args);
    }


    /**
     * 执行脚本 返回long
     * @param redisScript 脚本
     * @param keys 字段名称
     * @param args 值
     * @return 返回值
     */
    public Long executeLong(RedisScript<Long> redisScript, List<String> keys, String... args){
        return (Long) stringRedisTemplate.execute(redisScript, keys, args);
    }


    /**
     * 获取对应key过期时间,单位为秒
     *
     * @param key key
     * @return 剩余过期时长  单位秒
     */
    public Long getKeyTtlForSeconds(String key) {
        return stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
    }


    /**
     * 设置TTL时间
     *
     * @param key key
     * @param expireTime 过期时长 秒
     * @return 成功true
     */
    public boolean expire(final String key, Long expireTime) {
        try {
            stringRedisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            return true;
        } catch (Exception ex) {
            log.error("设置TTL出错:key：{}，expireTime：{}", key, expireTime, ex);
        }
        return false;
    }

    /**
     * 如果不存在设置
     * @param key key
     * @param value 值
     * @return 是否成功
     */
    public boolean setIfAbsent(String key, Object value) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        return Boolean.TRUE.equals(valueOperations.setIfAbsent(key, value));
    }


    /**
     * 获取所有的KEY
     * @return key表达式
     */
    public List<String> getKeys(String patternKey){
        ScanOptions options = ScanOptions.scanOptions()
                .count(10000)
                .match(patternKey).build();
        RedisSerializer<String> redisSerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
        Cursor<String> cursor = redisTemplate.executeWithStickyConnection(redisConnection -> new ConvertingCursor<>(redisConnection.scan(options), redisSerializer::deserialize));
        List<String> keys = new ArrayList<>();
        while(true){
            assert cursor != null;
            if (!cursor.hasNext()) break;
            keys.add(Objects.requireNonNull(cursor.next()).toString());
        }
        try {
            cursor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return keys;
    }
}