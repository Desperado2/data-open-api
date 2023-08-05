package com.github.desperado2.data.open.api.engine.manage.function;


import com.github.desperado2.data.open.api.engine.manage.ApiInfoContent;
import com.github.desperado2.data.open.api.engine.manage.model.MaskParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

/**
 * 工具类
 */
@Component
public class DataMaskingFunction implements IFunction{

    @Autowired
    private ApiInfoContent apiInfoContent;

    @Override
    public String getVarName() {
        return "DataMasking";
    }

    /**
     * 绑定SQL参数
     * @param varName 参数名称
     * @param value 参数值
     */
    public void bindValue(String varName, String maskType, Object... value){
        // 绑定参数到apiInfo
        LinkedHashMap<String, MaskParams> dbParamBinds = apiInfoContent.getDataMaskingBinds();
        if(dbParamBinds == null){
            apiInfoContent.setDbParamBinds(new LinkedHashMap<String, Object>(){{
                put(varName, new MaskParams(maskType, value));
            }});
        }else {
            dbParamBinds.put(varName, new MaskParams(maskType, value));
        }
    }

}
