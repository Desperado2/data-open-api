package com.github.desperado2.data.open.api.common.manage.config;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtils {

    private static ApplicationContext applicationContext;

    public SpringContextUtils(ApplicationContext applicationContext){
        SpringContextUtils.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public ApplicationContext getContext(){
        return SpringContextUtils.applicationContext;
    }
}
