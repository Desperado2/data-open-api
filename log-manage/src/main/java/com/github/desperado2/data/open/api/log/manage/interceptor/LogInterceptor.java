package com.github.desperado2.data.open.api.log.manage.interceptor;


import com.alibaba.fastjson.JSONObject;
import com.github.desperado2.data.open.api.common.manage.config.RequestWrapper;
import com.github.desperado2.data.open.api.common.manage.constants.Constants;
import com.github.desperado2.data.open.api.common.manage.enums.ApiExecuteEnvironmentEnum;
import com.github.desperado2.data.open.api.common.manage.utils.RequestUtils;
import com.github.desperado2.data.open.api.common.manage.utils.UUIDUtils;
import com.github.desperado2.data.open.api.log.manage.enums.LogEventEnums;
import com.github.desperado2.data.open.api.log.manage.event.LogEvent;
import com.github.desperado2.data.open.api.log.manage.model.LogEventModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Map;


/**
 * 日志拦截器
 * @author tu nan
 * @date 2023/2/13
 **/
@Component
public class LogInterceptor extends HandlerInterceptorAdapter {

    private static final Logger log = LoggerFactory.getLogger(LogInterceptor.class);

    private final ApplicationEventPublisher applicationEventPublisher;

    public LogInterceptor(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            LogEventModel logEventModel = new LogEventModel();
            // 获取参数
            String pattern = RequestUtils.buildPattern(request);
            String logKey = UUIDUtils.getUUID();
            RequestWrapper requestWrapper = new RequestWrapper(request);
            String apiRequestEnvironment = request.getHeader(Constants.API_REQUEST_ENVIRONMENT);
            String body = requestWrapper.getBody();
            JSONObject jsonObject = new JSONObject();
            for (Map.Entry<String, String[]> stringEntry : requestWrapper.getParameterMap().entrySet()) {
                jsonObject.put(stringEntry.getKey(), stringEntry.getValue()[0]);
            }
            logEventModel.setRequestEnvironment(ApiExecuteEnvironmentEnum.getNameByCode(apiRequestEnvironment));
            logEventModel.setParams(jsonObject);
            logEventModel.setBody(body);
            logEventModel.setRequestUrl(pattern);
            logEventModel.setAddress(request.getRequestURI());
            logEventModel.setMethod(request.getMethod().toUpperCase(Locale.ROOT));
            logEventModel.setLogEventEnums(LogEventEnums.ADD);
            logEventModel.setLogKey(logKey);
            // 发布事件
            applicationEventPublisher.publishEvent(new LogEvent(logEventModel));
            request.setAttribute(Constants.LOG_KEY, logKey);
        }catch (Exception e){
            log.error("请求日志解析错误",e);
        }
        return true;
    }
}
