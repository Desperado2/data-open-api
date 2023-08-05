package com.github.desperado2.data.open.api.log.manage.listener;


import com.alibaba.fastjson.JSONObject;
import com.github.desperado2.data.open.api.cache.manage.chche.IApiApplyInfoCache;
import com.github.desperado2.data.open.api.cache.manage.chche.IApiInfoCache;
import com.github.desperado2.data.open.api.cache.manage.chche.IKeySecretCache;
import com.github.desperado2.data.open.api.cache.manage.chche.ILogCache;
import com.github.desperado2.data.open.api.cache.manage.model.ApiApplyInfo;
import com.github.desperado2.data.open.api.cache.manage.model.ApiInfo;
import com.github.desperado2.data.open.api.cache.manage.model.ApiLogsCache;
import com.github.desperado2.data.open.api.cache.manage.model.KeySecretInfo;
import com.github.desperado2.data.open.api.common.manage.enums.ExternalResultCodeEnum;
import com.github.desperado2.data.open.api.common.manage.model.BaseKeySecretModel;
import com.github.desperado2.data.open.api.common.manage.model.ExternalResult;
import com.github.desperado2.data.open.api.common.manage.utils.BeanUtil;
import com.github.desperado2.data.open.api.log.manage.entity.ApiLogs;
import com.github.desperado2.data.open.api.log.manage.enums.LogEventEnums;
import com.github.desperado2.data.open.api.log.manage.enums.RequestStatusEnums;
import com.github.desperado2.data.open.api.log.manage.event.LogEvent;
import com.github.desperado2.data.open.api.log.manage.model.LogEventModel;
import com.github.desperado2.data.open.api.log.manage.service.IApiLogsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 日志事件監聽器
 * @author tu nan
 * @date 2023/2/13
 **/
@Component
public class LogEventListener implements ApplicationListener<LogEvent> {

    private final IKeySecretCache keySecretCache;

    private final IApiInfoCache apiInfoCache;

    private final IApiApplyInfoCache apiApplyInfoCache;

    private final IApiLogsService apiLogsService;

    private final ILogCache logCache;

    public LogEventListener(IKeySecretCache keySecretCache, IApiInfoCache apiInfoCache, IApiLogsService apiLogsService,
                            ILogCache logCache,IApiApplyInfoCache apiApplyInfoCache) {
        this.keySecretCache = keySecretCache;
        this.apiInfoCache = apiInfoCache;
        this.apiLogsService = apiLogsService;
        this.logCache = logCache;
        this.apiApplyInfoCache = apiApplyInfoCache;
    }

    @Override
    public void onApplicationEvent(LogEvent logEvent) {
        LogEventModel logEventModel = (LogEventModel)logEvent.getSource();
        if(LogEventEnums.ADD == logEventModel.getLogEventEnums()){
            addLog(logEventModel);
        }else {
            updateLog(logEventModel);
        }
    }

    /**
     * 新增日志   暂时存缓存，后续入库
     * @param logEventModel 日志信息
     */
    private void addLog(LogEventModel logEventModel){
        ApiLogsCache apiLogsCache = new ApiLogsCache();
        String body = logEventModel.getBody();
        JSONObject params = logEventModel.getParams();
        String requestURI = logEventModel.getAddress();
        String method = logEventModel.getMethod();
        // 获取用户
        BaseKeySecretModel baseKeySecretParam = new BaseKeySecretModel();
        if(StringUtils.isNotBlank(body)){
            baseKeySecretParam = JSONObject.parseObject(body, BaseKeySecretModel.class);
        }else{
            baseKeySecretParam = JSONObject.parseObject(params.toJSONString(), BaseKeySecretModel.class);
        }
        String appKey = baseKeySecretParam.getAppKey();
        KeySecretInfo keySecretInfo = keySecretCache.get(KeySecretInfo.Builder.builder().appKey(appKey).build());
        if(keySecretInfo != null){
            apiLogsCache.setUserId(keySecretInfo.getUserId());
        }
        apiLogsCache.setRequestEnvironment(logEventModel.getRequestEnvironment());
        apiLogsCache.setRequestUrl(logEventModel.getRequestUrl());
        apiLogsCache.setAppKey(appKey);
        apiLogsCache.setRequestParams(StringUtils.isNotBlank(body) ? body : params.toJSONString());
        apiLogsCache.setLogKey(logEventModel.getLogKey());
        apiLogsCache.setRequestTime(System.currentTimeMillis());
        ApiInfo apiInfo = apiInfoCache.get(ApiInfo.Builder.builder().path(requestURI).method(method).build());
        if(apiInfo != null){
            apiLogsCache.setApiId(apiInfo.getId());
            apiLogsCache.setApiName(apiInfo.getApiName());
        }
        ApiApplyInfo apiApplyInfo = apiApplyInfoCache.get(ApiApplyInfo.Builder.builder()
                .apiId(apiLogsCache.getApiId()).userId(apiLogsCache.getUserId()).build());
        if(apiApplyInfo != null){
            apiLogsCache.setUsername(apiApplyInfo.getUsername());
        }
        logCache.put(apiLogsCache);
    }

    /**
     * 日志入库
     * @param logEventModel 日志信息
     */
    private void updateLog(LogEventModel logEventModel){
        // 更新
        String logKey = logEventModel.getLogKey();
        if(StringUtils.isBlank(logKey)){
            return;
        }
        ApiLogsCache apiLogsCache = logCache.get(ApiLogsCache.Builder.builder().logKey(logKey).build());
        ApiLogs apiLogs = null;
        if(apiLogsCache != null){
            apiLogs = BeanUtil.sourceToTarget(apiLogsCache, ApiLogs.class);
        }else{
            apiLogs = apiLogsService.getByKey(logKey);
        }
        if(apiLogs != null){
            // 转换
            String responseBody = logEventModel.getResponseBody();
            ExternalResult dataResult = JSONObject.parseObject(responseBody, ExternalResult.class);
            apiLogs.setResultCount(dataResult.getDataSize());
            apiLogs.setErrorMsg(dataResult.getMessage());
            apiLogs.setResponseTime(System.currentTimeMillis());
            apiLogs.setCreateTime(new Date());
            if(dataResult.getReturnCode().equals(ExternalResultCodeEnum.SUCCESS.getCode())){
                apiLogs.setStatus(RequestStatusEnums.SUCCESS.getCode());
            }else{
                apiLogs.setStatus(RequestStatusEnums.FAIL.getCode());
            }
            apiLogsService.saveOrUpdate(apiLogs);
        }
    }
}
