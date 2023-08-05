package com.github.desperado2.data.open.api.engine.manage.scripts.javascript;//package com.github.desperado2.open.data.platform.api.manage.extend.scripts;

/**
 * js脚本执行器
 */

import com.github.desperado2.data.open.api.cache.manage.model.ApiInfo;
import com.github.desperado2.data.open.api.common.manage.enums.ApiExecuteEnvironmentEnum;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.engine.manage.ApiInfoContent;
import com.github.desperado2.data.open.api.engine.manage.function.IFunction;
import com.github.desperado2.data.open.api.engine.manage.model.ApiParams;
import com.github.desperado2.data.open.api.engine.manage.scripts.IScriptParse;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service("javaScriptScriptExecutor")
public class JavaScriptScriptParse implements IScriptParse {

    @Autowired
    private ApiInfoContent apiInfoContent;

    @Autowired
    private ApplicationContext context;

    private Collection<IFunction> functionList;

    @PostConstruct
    public void init(){
        //加载函数
        functionList = context.getBeansOfType(IFunction.class).values();
    }

    @Override
    @Transactional
    public Object runScript(ApiExecuteEnvironmentEnum environmentEnum, String script, ApiInfo apiInfo, ApiParams apiParams) throws Throwable {
        try {
            //注入变量
            boolean safe = KeywordCheckUtils.isSafeJavScriptScript(script);
            if(!safe){
               throw new DataOpenPlatformException("脚本检测为恶意脚本");
            }
            apiInfoContent.setApiInfo(apiInfo);
            apiInfoContent.setApiParams(apiParams);

            //注入函数
            String scriptContent = "function run(){" +
                    script +
                    "}";
            ScriptEngineManager factory = new ScriptEngineManager();
            ScriptEngine engine = factory.getEngineByName("js");

            for(IFunction function : functionList){
                engine.put(function.getVarName(),function);
            }
            //注入属性变量
            Bindings bindings = engine.createBindings();
            Map<String, Object> stringObjectMap = buildScriptParams(apiParams, bindings);
            for (Map.Entry<String, Object> entry : stringObjectMap.entrySet()) {
                engine.put(entry.getKey(),entry.getValue());
            }
            apiInfoContent.setApiExecuteEnvironmentEnum(environmentEnum);
            //注入属性变量
            apiInfoContent.setRequestParams(stringObjectMap);

            engine.eval(scriptContent);
            Invocable inv = (Invocable) engine;
            Object result = inv.invokeFunction("run");
            if(result == null) {
                return null;
            }
            if (!(result instanceof ScriptObjectMirror)){
                return result;
            }
            // 遍历所有值
            ScriptObjectMirror scriptObject = (ScriptObjectMirror) result;
            // 递归处理查询结果的数组
            recursionEencapsulation(scriptObject);

            Boolean isArray = isArray(scriptObject);
            if(isArray) {
                return scriptObject.values();
            }
            return scriptObject;
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public Object engineEval(String script, Bindings bindings) throws Throwable {
        return null;
    }

    // 递归封装数组
    private void recursionEencapsulation(ScriptObjectMirror scriptObject) {
        for(String key : scriptObject.keySet()){
            Object mirror = scriptObject.get(key);
            if (mirror instanceof ScriptObjectMirror) {
                recursionEencapsulation((ScriptObjectMirror) mirror);
                Boolean isArray = isArray((ScriptObjectMirror) mirror);
                if(isArray) {
                    List<Object> list = parseArray((ScriptObjectMirror) mirror);
                    scriptObject.put(key, list);
                }
            }
        }
    }

    // 判断是否为数组
    private Boolean isArray(ScriptObjectMirror scriptObject) {
        boolean isArray = true;
        int index = 0;
        for(String key : scriptObject.keySet()){
            if(!key.equals(index+"")) {
                isArray = false;
            }
            index++;
        }
        return isArray;
    }

    // 将数组转为ArrayList
    private List<Object> parseArray(ScriptObjectMirror scriptObject) {
        List<Object> arrayList = new ArrayList<Object>();
        for(String key : scriptObject.keySet()){
            Object obj = scriptObject.get(key);
            arrayList.add(obj);
        }
        return arrayList;
    }
}
