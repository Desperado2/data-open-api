package com.github.desperado2.data.open.api.security.manage.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;


/**
 * 加载redis的lua脚本
 * @author tu nan
 * @date 2021/3/11
 **/
@Configuration
public class RedisLuaScript {


    @Bean
    public DefaultRedisScript<Long> defaultRedisScript(){
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(
                new ClassPathResource("/META-INF/lua/redis_request_queue.lua")));
        redisScript.setResultType(Long.class);
        return redisScript;
    }
}
