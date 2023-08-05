package com.github.desperado2.data.open.api.common.manage.constants;

/**
 * 统一的redis的key值管理类
 *
 * @author liujianqiang
 * @date 2018年9月5日
 */
public class RedisKey {

    /**
     * 登录token信息
     */
    public static final String LOGIN_TOKEN = "open:data:platform:token:";

    /**
     *  根据码限流
     */
    public static final String LIMIT_BY_APPKEY = "open:data:platform:limit:by:appkey:";

    /**
     * 根据ip限流
     */
    public static final String LIMIT_BY_IP = "open:data:platform:limit:by:ip:";


    /**
     * 验证吗token信息
     */
    public static final String USER_LOGIN_IDENTIFYING_CODE = "open:data:platform:identifying:code:";


    /**
     * 用户登入系统时，输错密码次数key
     * */
    public static final String USER_LOGIN_ERROR_COUNT_CODE = "open:data:platform:user:login:error:count:";


    /**
     * 用户更新之后的缓存，用于更新用户的登录信息
     */
    public static final String CACHE_USER_UPDATE = "open:data:platform:cache:user:update:";

}
