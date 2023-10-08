package com.github.desperado2.data.open.api.engine.manage;


import com.github.desperado2.data.open.api.cache.manage.model.ApiInfo;
import com.github.desperado2.data.open.api.common.manage.enums.ApiExecuteEnvironmentEnum;
import com.github.desperado2.data.open.api.engine.manage.model.ApiParams;
import com.github.desperado2.data.open.api.engine.manage.model.MaskParams;
import org.springframework.stereotype.Component;

import javax.script.Bindings;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 脚本执行上下文变量
 * @author tu nan
 * @date 2023/3/13
 **/
@Component
public class ApiInfoContent {

    private ThreadLocal<Boolean> isDebug = new InheritableThreadLocal<>();
    private ThreadLocal<Boolean> isLocalTest = new InheritableThreadLocal<>();
    private ThreadLocal<ApiInfo> apiInfo = new InheritableThreadLocal<>();
    private ThreadLocal<ApiExecuteEnvironmentEnum> apiExecuteEnvironmentEnum = new InheritableThreadLocal<>();
    private ThreadLocal<ApiParams> apiParams = new InheritableThreadLocal<>();
    private ThreadLocal<List<String>> logs = new InheritableThreadLocal<>();
    private ThreadLocal<Bindings> engineBindings = new InheritableThreadLocal<>();
    private ThreadLocal<Map> requestParams = new InheritableThreadLocal<>();
    private ThreadLocal<LinkedHashMap<String, Object>> dbParamBinds = new InheritableThreadLocal<>();
    private ThreadLocal<LinkedHashMap<String, MaskParams>> dataMaskingBinds = new InheritableThreadLocal<>();

    public Boolean getIsDebug() {
        return isDebug.get() != null && isDebug.get();
    }

    public void setIsDebug(Boolean isDebug) {
        this.isDebug.set(isDebug);
    }
    public Boolean getIsLocalTest() {
        return isLocalTest.get() != null && isLocalTest.get();
    }

    public void setIsLocalTest(Boolean isLocalTest) {
        this.isLocalTest.set(isLocalTest);
    }

    public ApiInfo getApiInfo() {
        return apiInfo.get();
    }

    public void setApiInfo(ApiInfo apiInfo) {
        this.apiInfo.set(apiInfo);
    }

    public ApiParams getApiParams() {
        return apiParams.get();
    }

    public void setApiParams(ApiParams apiParams) {
        this.apiParams.set(apiParams);
    }

    public ApiExecuteEnvironmentEnum getApiExecuteEnvironmentEnum() {
        return apiExecuteEnvironmentEnum.get();
    }

    public void setApiExecuteEnvironmentEnum(ApiExecuteEnvironmentEnum apiExecuteEnvironmentEnum) {
        this.apiExecuteEnvironmentEnum.set(apiExecuteEnvironmentEnum);
    }

    public Map getRequestParams() {
        return requestParams.get();
    }

    public void setRequestParams(Map requestParams) {
        this.requestParams.set(requestParams);
    }

    public List<String> getLogs() {
        return logs.get();
    }

    public void putLog(String log){
        if (logs.get() == null){
            logs.set(new ArrayList<>());
        }
        logs.get().add(log);
    }

    public Bindings getEngineBindings() {
        return engineBindings.get();
    }

    public void setEngineBindings(Bindings engineBindings) {
        this.engineBindings.set(engineBindings);
    }

    public LinkedHashMap<String, Object> getDbParamBinds() {
        return dbParamBinds.get();
    }

    public void setDbParamBinds(LinkedHashMap<String, Object> dbParamBinds) {
        this.dbParamBinds.set(dbParamBinds);
    }

    public LinkedHashMap<String, MaskParams> getDataMaskingBinds() {
        return dataMaskingBinds.get();
    }

    public void setDataMaskingBinds(LinkedHashMap<String, MaskParams> dataMaskingBinds) {
        this.dataMaskingBinds.set(dataMaskingBinds);
    }

    public void  removeAll(){
        apiInfo.remove();
        apiParams.remove();
        logs.remove();
        engineBindings.remove();
        isDebug.remove();
        requestParams.remove();
        dbParamBinds.remove();
        dataMaskingBinds.remove();
    }
}
