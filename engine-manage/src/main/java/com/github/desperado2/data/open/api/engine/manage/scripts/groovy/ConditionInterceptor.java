package com.github.desperado2.data.open.api.engine.manage.scripts.groovy;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * 拦截器
 * @author tu nan
 * @date 2023/3/30
 **/
public class ConditionInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ConditionInterceptor.class);

    static boolean checkTimeout(int timeout){
        boolean flag = ThreadLocalUtils.getStartTime() + timeout * 1000L < System.currentTimeMillis();
        if(flag){
            logger.error("[{}] Execution timed out after {} seconds. Start Time:{}",
                    ThreadLocalUtils.get("scriptName"),
                    timeout,
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(ThreadLocalUtils.getStartTime()), ZoneOffset.ofHours(8))
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            );
        }
        return flag;
    }

}
