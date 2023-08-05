package com.github.desperado2.data.open.api.engine.manage.function;

import com.github.desperado2.data.open.api.common.manage.enums.ApiExecuteEnvironmentEnum;
import com.github.desperado2.data.open.api.engine.manage.ApiInfoContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * spring环境变量函数
 */
@Component
public class EnvFunction implements IFunction{

    @Autowired
    private ApiInfoContent apiInfoContent;

    @Override
    public String getVarName() {
        return "env";
    }

    public Boolean isTest(){
        return ApiExecuteEnvironmentEnum.TEST == apiInfoContent.getApiExecuteEnvironmentEnum();
    }

    public Boolean isPre(){
        return ApiExecuteEnvironmentEnum.PRE == apiInfoContent.getApiExecuteEnvironmentEnum();
    }

    public Boolean isProd(){
        return ApiExecuteEnvironmentEnum.PROD == apiInfoContent.getApiExecuteEnvironmentEnum();
    }

    public String getExecuteEnv(){
        return apiInfoContent.getApiExecuteEnvironmentEnum().getCode();
    }
}
