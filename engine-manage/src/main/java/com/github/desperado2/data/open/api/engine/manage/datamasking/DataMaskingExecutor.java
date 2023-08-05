package com.github.desperado2.data.open.api.engine.manage.datamasking;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 脱敏执行器
 * @author tu nan
 * @date 2023/3/29
 **/
@Component
public class DataMaskingExecutor implements ApplicationContextAware {

    public Map<String, IDataMaskingService> executorMap = new HashMap<>();


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        executorMap = applicationContext.getBeansOfType(IDataMaskingService.class);
    }

    /**
     * 获取执行器
     * @param dataMaskingEnum 执行器枚举
     * @return 具体执行器
     */
    public IDataMaskingService getScriptExecutor(DataMaskingEnum dataMaskingEnum) {
//        if(!executorMap.containsKey(dataMaskingEnum.getExecutorBeanName())){
//            throw new DataOpenPlatformException("无效的脱敏类型");
//        }
        return executorMap.get(dataMaskingEnum.getExecutorBeanName());
    }
}
