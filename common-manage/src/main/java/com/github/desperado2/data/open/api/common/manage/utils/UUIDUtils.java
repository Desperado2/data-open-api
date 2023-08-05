package com.github.desperado2.data.open.api.common.manage.utils;


import java.util.UUID;

/**
 * UUID工具类
 * @author tu nan
 * @date 2023/2/10
 **/
public class UUIDUtils {

    private UUIDUtils() {}

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
