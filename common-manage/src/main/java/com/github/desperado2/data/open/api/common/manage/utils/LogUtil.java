package com.github.desperado2.data.open.api.common.manage.utils;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 日志工具
 * @author tu nan
 * @date 2023/4/11
 **/
public class LogUtil {
    static LogUtil instanced = new LogUtil();
    private static final String LOG_UUID = "LOG_UUID";
    private InheritableThreadLocal<Map<String, String>> inheritableThreadLocal = new InheritableThreadLocal<Map<String, String>>() {
        protected Map<String, String> childValue(Map<String, String> parentValue) {
            return parentValue == null ? null : new HashMap(parentValue);
        }
    };

    private LogUtil() {
    }

    private void put(String key, String val) {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        } else {
            Map<String, String> map = (Map)this.inheritableThreadLocal.get();
            if (map == null) {
                map = new HashMap();
                this.inheritableThreadLocal.set(map);
            }

            ((Map)map).put(key, val);
        }
    }

    private String get(String key) {
        Map<String, String> map = (Map)this.inheritableThreadLocal.get();
        if (map == null) {
            map = new HashMap();
            this.inheritableThreadLocal.set(map);
        }

        return (String)((Map)map).get(key);
    }

    public String updateLogId(String logID) {
        if (null != logID && !"".equals(logID)) {
            this.put("LOG_UUID", logID);
        } else {
            logID = this.createLogId();
            this.put("LOG_UUID", logID);
        }

        return logID;
    }

    public String createLogId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String getLogId() {
        Object object = instanced.get("LOG_UUID");
        return instanced.updateLogId(object == null ? "" : object.toString());
    }
}
