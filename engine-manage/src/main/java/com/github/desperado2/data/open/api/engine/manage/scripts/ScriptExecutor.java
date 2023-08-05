package com.github.desperado2.data.open.api.engine.manage.scripts;


import com.github.desperado2.data.open.api.common.manage.enums.ScriptTypeEnum;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 脚本执行器
 * @author tu nan
 * @date 2023/3/29
 **/
@Component
public class ScriptExecutor implements ApplicationContextAware {

    public Map<String, IScriptParse> executorMap = new HashMap<>();


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        executorMap = applicationContext.getBeansOfType(IScriptParse.class);
    }

    /**
     * 获取执行器
     * @param scriptTypeEnum 执行器枚举
     * @return 具体执行器
     */
    public IScriptParse getScriptExecutor(ScriptTypeEnum scriptTypeEnum) throws DataOpenPlatformException {
        if(!executorMap.containsKey(scriptTypeEnum.getExecutorBeanName())){
            throw new DataOpenPlatformException("无效的脚本类型");
        }
        return executorMap.get(scriptTypeEnum.getExecutorBeanName());
    }
}
