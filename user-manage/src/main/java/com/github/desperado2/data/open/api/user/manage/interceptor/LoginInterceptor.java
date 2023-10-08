package com.github.desperado2.data.open.api.user.manage.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.desperado2.data.open.api.cache.manage.chche.IUserTokenCache;
import com.github.desperado2.data.open.api.common.manage.model.DataResult;
import com.github.desperado2.data.open.api.user.manage.config.UserConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * 登录拦截器
 * @author tu nan
 * @date 2021/3/11
 **/
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

    private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);

    private final IUserTokenCache userTokenCache;

    private final UserConfig userConfig;

    public LoginInterceptor(IUserTokenCache userTokenCache, UserConfig userConfig) {
        this.userTokenCache = userTokenCache;
        this.userConfig = userConfig;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if(StringUtils.isNotBlank(token) && userTokenCache.exists(token)){
            // 存在，判断是否快过期
            long keyTtlForSeconds = userTokenCache.getKeyTtlForSeconds(token);
            // PC端，续期token
            if(keyTtlForSeconds < userConfig.getTokenRefreshTokenEndTime()){
                // 续期token，刷新cookies
                userTokenCache.expire(token, userConfig.getTokenTimeout());
                refreshCookie(request, response, userConfig.getTokenTimeout());
            }
            // 绑定session
            bindToken2Session(request, token);
            return true;
        }else{
            Cookie[] cookies = request.getCookies();
            if (cookies == null || cookies.length == 0) {
                log.error("cookie is null. 请求的uri-->{}:{}", request.getMethod(), request.getRequestURI());
                response.addHeader("Content-Type", "application/json");
                response.setCharacterEncoding("utf-8");
                response.getWriter().write(JSON.toJSONString(new DataResult<>(401, "您暂未登入")));
                return false;
            } else {
                for (Cookie cookie : cookies) {
                    // 不存在super-token继续下一个循环
                    if (!StringUtils.equals(cookie.getName(), "token")) {
                        continue;
                    }
                    // cookie中获取super-token的值
                    token = cookie.getValue();
                    // Redis是否存在
                    if (StringUtils.isNotBlank(token) && userTokenCache.exists(token)) {
                        long keyTtl = userTokenCache.getKeyTtlForSeconds(token);
                        if (keyTtl < userConfig.getTokenRefreshTokenEndTime()) {
                            userTokenCache.expire(token, userConfig.getTokenTimeout());
                            refreshCookie(request, response, userConfig.getTokenTimeout());
                        }
                        bindToken2Session(request, token);
                        return true;
                    }
                }
                log.error("cookie is null. 请求的uri-->{}:{}", request.getMethod(), request.getRequestURI());
                response.addHeader("Content-Type", "application/json");
                response.setCharacterEncoding("utf-8");
                response.getWriter().write(JSON.toJSONString(new DataResult<>(401, "您暂未登入")));
                return false;
            }
        }
    }


    private void bindToken2Session(HttpServletRequest request, String token) {
        HttpSession session = request.getSession();
        session.setAttribute("token", token);
    }

    /**
     * 返回异常信息
     * @param response 响应
     */
    private void returnMsg(HttpServletResponse response){
        DataResult<String> richResult = new DataResult<>(401, "您暂未登入");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        String jsonObject = JSONObject.toJSONString(richResult);
        try {
            out = response.getWriter();
            out.append(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 刷新cookie
     *
     * @param request 请求
     * @param response 响应
     * @param tokenTimeout token超时时长
     */
    private void refreshCookie(HttpServletRequest request, HttpServletResponse response, Long tokenTimeout) {
        // 获取Cookies数组
        Cookie[] cookies = request.getCookies();
        Cookie update;
        // 迭代查找并清除Cookie
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (StringUtils.equals(cookie.getName(), "token")) {
                    update = new Cookie(cookie.getName(), cookie.getValue());
                    update.setPath("/");
                    update.setDomain(userConfig.getCookieDomain());
                    update.setMaxAge(Math.toIntExact(tokenTimeout));
                    response.addCookie(update);
                }
            }
        }
    }
}
