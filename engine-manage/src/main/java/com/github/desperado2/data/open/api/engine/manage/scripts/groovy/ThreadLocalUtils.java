package com.github.desperado2.data.open.api.engine.manage.scripts.groovy;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 工具类
 * @author tu nan
 * @date 2023/3/30
 **/
public class ThreadLocalUtils {

    private static final  ThreadLocal<Map<String, Object>> THREAD_LOCAL = ThreadLocal.withInitial(HashMap::new);

    public static long getStartTime() {
        return (long) THREAD_LOCAL.get().get("__startTime");
    }

    public static void setStartTime() {
        THREAD_LOCAL.get().put("__startTime", System.currentTimeMillis());
    }

    public static void set(String key, Object value) {
        THREAD_LOCAL.get().put(key, value);
    }

    public static Object get(String key) {
        Map<String, Object> map = THREAD_LOCAL.get();
        if (Objects.isNull(map)) {
            return null;
        }
        return map.get(key);
    }

    public static void setAll(Map<String, Object> map) {
        THREAD_LOCAL.get().putAll(map);
    }
}
