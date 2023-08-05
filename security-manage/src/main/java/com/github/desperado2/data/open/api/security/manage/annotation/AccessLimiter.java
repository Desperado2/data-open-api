package com.github.desperado2.data.open.api.security.manage.annotation;


import com.github.desperado2.data.open.api.common.manage.enums.AccessLimiterEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口限流注解
 * @author tu nan
 * @date 2021/3/11
 **/
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessLimiter {


    /**
     * 请求限制数
     */
    int limit();


    /**
     * 时间范围,秒
     */
    int timeScope();


    /**
     * 限流方式集合
     */
    AccessLimiterEnum[] limiterType();

    /**
     * 返回值对象。默认字符串
     */
    Class<?> returnType() default String.class;
}
