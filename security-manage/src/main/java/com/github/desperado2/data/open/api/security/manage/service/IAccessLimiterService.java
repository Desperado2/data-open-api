package com.github.desperado2.data.open.api.security.manage.service;



/**
 * 限流实现
 * @author tu nan
 * @date 2023/3/27
 **/
public interface IAccessLimiterService {

    /**
     * 验证
     * @param key key标识
     * @param limit 限制数
     * @param timeScope 时间范围
     * @return 是否通过
     */
    boolean verify(String key, Integer limit, Integer timeScope);
}
