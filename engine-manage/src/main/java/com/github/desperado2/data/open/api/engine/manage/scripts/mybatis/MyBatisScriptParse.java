package com.github.desperado2.data.open.api.engine.manage.scripts.mybatis;


import com.github.desperado2.data.open.api.cache.manage.model.ApiInfo;
import com.github.desperado2.data.open.api.common.manage.enums.ApiExecuteEnvironmentEnum;
import com.github.desperado2.data.open.api.datasource.manage.datasource.DataSourceManager;
import com.github.desperado2.data.open.api.engine.manage.ApiInfoContent;
import com.github.desperado2.data.open.api.engine.manage.enums.ExecuteType;
import com.github.desperado2.data.open.api.engine.manage.function.DbFunction;
import com.github.desperado2.data.open.api.engine.manage.model.ApiParams;
import com.github.desperado2.data.open.api.engine.manage.scripts.IScriptParse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.script.Bindings;
import javax.script.SimpleBindings;

/**
 * mybatis执行器
 * @author tu nan
 * @date 2023/3/13
 **/
@Service("mybatisScriptExecutor")
public class MyBatisScriptParse implements IScriptParse {

    @Autowired
    private ApiInfoContent apiInfoContent;

    @Autowired
    private DbFunction dbFunction;


    @Override
    public Object runScript(ExecuteType executeType,ApiExecuteEnvironmentEnum environmentEnum, String script, ApiInfo apiInfo, ApiParams apiParams) throws Throwable {
        //注入变量
        apiInfoContent.setIsLocalTest(ExecuteType.SYS == executeType);
        apiInfoContent.setApiInfo(apiInfo);
        apiInfoContent.setApiParams(apiParams);
        Bindings bindings = new SimpleBindings();
        apiInfoContent.setEngineBindings(bindings);
        //注入属性变量
        apiInfoContent.setApiExecuteEnvironmentEnum(environmentEnum);
        apiInfoContent.setRequestParams(buildScriptParams(apiParams, bindings));
        return dbFunction.selectList(script);
    }

    @Override
    public Object engineEval(String script, Bindings bindings) throws Throwable {
        return null;
    }
}
