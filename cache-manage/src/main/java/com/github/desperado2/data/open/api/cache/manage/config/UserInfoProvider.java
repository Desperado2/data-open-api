package com.github.desperado2.data.open.api.cache.manage.config;


import com.github.desperado2.data.open.api.cache.manage.chche.IUserTokenCache;
import com.github.desperado2.data.open.api.common.manage.constants.RedisKey;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformRuntimeException;
import com.github.desperado2.data.open.api.common.manage.model.BaseUserModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


/**
 * 登录用户信息助手类
 * @author tu nan
 * @date 2021/3/11
 **/
@Component
public class UserInfoProvider {

    protected final HttpServletRequest request;

    protected final HttpServletResponse response;

    private final IUserTokenCache userTokenCache;

    @Value("${open.data.platform.cookie.domain}")
    private String cookieDomain;

    public UserInfoProvider(HttpServletRequest request, HttpServletResponse response, IUserTokenCache userTokenCache) {
        this.request = request;
        this.response = response;
        this.userTokenCache = userTokenCache;
    }

    public BaseUserModel getLoginUserInfo(){
        String superToken = getSuperToken();
        BaseUserModel baseUserVO = userTokenCache.get(superToken);
        if(baseUserVO == null){
            // 不存在信息，直接返回
            return null;
        }
        // 判断user是否已更新
        String updateLoginRedisKey = RedisKey.CACHE_USER_UPDATE + baseUserVO.getId();
        BaseUserModel updateUserVO = userTokenCache.get(updateLoginRedisKey);
        if(updateUserVO == null){
            // 不存在更新信息，直接返回
            return baseUserVO;
        }
        // 更新登录信息
        Date updateTime = baseUserVO.getUpdateTime();
        Date newUpdateTime = updateUserVO.getUpdateTime();
        if(updateTime != null && newUpdateTime != null && newUpdateTime.getTime() <= updateTime.getTime()){
            // 表示数据未更新，直接返回
            return baseUserVO;
        }
        // 数据已更新，刷新数据
        userTokenCache.set(superToken, updateUserVO, userTokenCache.getKeyTtlForSeconds(superToken));
        return updateUserVO;
    }


    public String getSuperToken(){
        Object token = request.getSession().getAttribute("token");
        if(token == null){
            throw new DataOpenPlatformRuntimeException("您暂未登入", 401);
        }
        return (String) token;
    }

    public void addCookie(String name, String value, String path, Integer maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setVersion(1);
        cookie.setMaxAge(maxAge);
        cookie.setPath(path);
        cookie.setDomain(cookieDomain);
        response.addCookie(cookie);
    }


    public void deleteCookies() {
        // 获取Cookies数组
        Cookie[] cookies = request.getCookies();
        // 迭代查找并清除Cookie
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                cookie.setVersion(1);
                cookie.setMaxAge(0);
                cookie.setPath("/");
                cookie.setDomain(cookieDomain);
                response.addCookie(cookie);
            }
        }
    }
}
