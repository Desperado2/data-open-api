package com.github.desperado2.data.open.api.security.manage.interceptor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 抽象限流
 * @author tu nan
 * @date 2023/4/6
 **/
@Component
public class AbstractAccessLimiterInterceptor  {

    private static final Logger log = LoggerFactory.getLogger(AccessLimiterInterceptor.class);

    @Value("${open.data.platform.base.access-limiter-open: false}")
    private boolean openAccessLimiter;

    @Autowired
    private ApplicationContext applicationContext;


    public HandlerInterceptorAdapter handlerInterceptorAdapter(){
        if(openAccessLimiter){
            return applicationContext.getBean(AccessLimiterInterceptor.class);
        }else{
            return applicationContext.getBean(FakeAccessLimiterInterceptor.class);
        }
    }


}
