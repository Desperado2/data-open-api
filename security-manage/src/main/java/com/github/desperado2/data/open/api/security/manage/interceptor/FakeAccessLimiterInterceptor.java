package com.github.desperado2.data.open.api.security.manage.interceptor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 接口限流拦截器
 * @author tu nan
 * @date 2021/3/11
 **/
@Component
@ConditionalOnProperty(name = "open.data.platform.base.access-limiter-open", havingValue = "false", matchIfMissing = true)
public class FakeAccessLimiterInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(FakeAccessLimiterInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        return true;
    }

}
