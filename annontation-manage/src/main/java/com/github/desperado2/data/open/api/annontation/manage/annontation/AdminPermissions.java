package com.github.desperado2.data.open.api.annontation.manage.annontation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 管理员权限授权注解,该注解表示只有管理员权限才能调用
 *
 * @author tu nan
 * @date 2023/5/22
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AdminPermissions {
}
