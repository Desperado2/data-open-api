package com.github.desperado2.data.open.api.alert.manage.service.factory;


import com.github.desperado2.data.open.api.alert.manage.service.IAlertService;
import com.github.desperado2.data.open.api.alert.manage.service.enmus.AlertServiceTypeEnum;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 告警服务
 * @author tu nan
 * @date 2023/4/28
 **/
@Component
public class AlertService implements ApplicationContextAware {

    public Map<String, IAlertService> alertServiceMap = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        alertServiceMap = applicationContext.getBeansOfType(IAlertService.class);
    }

    /**
     * 获取执行器
     * @param alertServiceTypeEnum 执行器枚举
     * @return 具体执行器
     */
    public IAlertService getScriptExecutor(AlertServiceTypeEnum alertServiceTypeEnum) {
        if(!alertServiceMap.containsKey(alertServiceTypeEnum.getExecutorBeanName())){
           return alertServiceMap.get(AlertServiceTypeEnum.LOG.getExecutorBeanName());
        }
        return alertServiceMap.get(alertServiceTypeEnum.getExecutorBeanName());
    }
}
