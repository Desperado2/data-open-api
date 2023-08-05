package com.github.desperado2.data.open.api.engine.manage.function;


import com.github.desperado2.data.open.api.engine.manage.ApiInfoContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

/**
 * 工具类
 */
@Component
public class DbValueFunction implements IFunction{

    @Autowired
    private ApiInfoContent apiInfoContent;

    @Override
    public String getVarName() {
        return "DbParamUtils";
    }

    /**
     * 绑定SQL参数
     * @param varName 参数名称
     * @param value 参数值
     */
    public void bindValue(String varName, Object value){
        // 绑定参数到apiInfo
        LinkedHashMap<String, Object> dbParamBinds = apiInfoContent.getDbParamBinds();
        if(dbParamBinds == null){
            apiInfoContent.setDbParamBinds(new LinkedHashMap<String, Object>(){{put(varName, value);}});
        }else {
            dbParamBinds.put(varName, value);
        }
    }

}
