package com.github.desperado2.data.open.api.security.manage.interceptor;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.desperado2.data.open.api.common.manage.config.RequestWrapper;
import com.github.desperado2.data.open.api.common.manage.constants.RedisKey;
import com.github.desperado2.data.open.api.common.manage.enums.AccessLimiterEnum;
import com.github.desperado2.data.open.api.common.manage.enums.ErrorCodeEnum;
import com.github.desperado2.data.open.api.common.manage.model.DataResult;
import com.github.desperado2.data.open.api.common.manage.utils.ip.IpUtils;
import com.github.desperado2.data.open.api.security.manage.annotation.AccessLimiter;
import com.github.desperado2.data.open.api.security.manage.service.IAccessLimiterService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 接口限流拦截器
 * @author tu nan
 * @date 2021/3/11
 **/
@Component
@ConditionalOnProperty(name = "open.data.platform.base.access-limiter-open", havingValue = "true")
public class AccessLimiterInterceptor extends HandlerInterceptorAdapter {

    private static final Logger log = LoggerFactory.getLogger(AccessLimiterInterceptor.class);

    private final IAccessLimiterService accessLimiterService;

    public AccessLimiterInterceptor(IAccessLimiterService accessLimiterService) {
        this.accessLimiterService = accessLimiterService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        boolean result = true;
        try {
            if(handler instanceof HandlerMethod){
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                //拿到注解
                AccessLimiter accessLimiter = handlerMethod.getMethodAnnotation(AccessLimiter.class);
                if(accessLimiter != null){
                    result = mixingLimiter(accessLimiter,request,response);
                }
            }
        }catch (Exception e){
            log.warn("异常，允许业务继续执行");
        }
        return result;
    }

    /**
     * 混合限流
     * @param accessLimiter 限流配置
     * @param request 请求
     * @return 结果
     */
     private boolean mixingLimiter(AccessLimiter accessLimiter, HttpServletRequest request,
                                  HttpServletResponse response){
        AtomicBoolean result = new AtomicBoolean(true);
        if(accessLimiter != null){
            // 做限流验证
            int limit = accessLimiter.limit();
            int timeScope = accessLimiter.timeScope();
            AccessLimiterEnum[] accessLimiterEnums = accessLimiter.limiterType();
            Class<?> returnType = accessLimiter.returnType();
            Arrays.stream(accessLimiterEnums).forEach(it -> {
                if(AccessLimiterEnum.APPKEY_LIMIT.equals(it)){
                    // 码
                    String appKey = getParameterValue("appKey", request);
                    result.set(result.get() && appKeyLimiter(appKey, limit, timeScope,response,returnType));
                }else if(AccessLimiterEnum.IP_LIMIT.equals(it)){
                    // ip
                    String ipAddress = IpUtils.getClientIpAddr(request);
                    result.set(result.get() && ipLimiter(ipAddress, limit, timeScope,response,returnType));
                }
            });
        }
        return result.get();
    }


    /**
     * AppKey限流
     * @param appKey 用户标志
     * @return 是否允许访问
     */
    private boolean appKeyLimiter(String appKey,int limit,int timeScope,HttpServletResponse response, Class<?> returnType){
        if(StringUtils.isBlank(appKey)){
            return true;
        }
        String redisKey = RedisKey.LIMIT_BY_APPKEY + appKey;
        boolean verify = accessLimiterService.verify(redisKey, limit, timeScope);
        if(!verify){
            returnMsg(response, ErrorCodeEnum.REQUEST_TOO_FREQUENT_CODE, returnType);
        }
        return verify;
    }

    /**
     * IP限流
     * @param ip ip地址
     * @return 是否允许访问
     */
    private boolean ipLimiter(String ip,int limit,int timeScope,HttpServletResponse response, Class<?> returnType){
        if(StringUtils.isBlank(ip)){
            return true;
        }
        String redisKey = RedisKey.LIMIT_BY_IP + ip;
        boolean verify = accessLimiterService.verify(redisKey, limit, timeScope);
        if(!verify){
            returnMsg(response, ErrorCodeEnum.REQUEST_TOO_FREQUENT_IP,returnType);
        }
        return verify;
    }


    /**
     * 获取值
     * @param key 参数名称
     * @param request 请求
     * @return 参数值
     */
    private String getParameterValue(String key,HttpServletRequest request){
        // 第一步。获取requestParam
        String parameter = request.getParameter(key);
        if(StringUtils.isNotBlank(parameter)){
            return parameter;
        }

        // 第二步，获取path
        NativeWebRequest webRequest = new ServletWebRequest(request);
        Object requestAttribute = webRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        if(requestAttribute != null){
            Map<String, Object> map = JSONObject.parseObject(JSON.toJSONString(requestAttribute), new TypeReference<Map<String, Object>>() {});
            if (map != null && map.containsKey(key)){
                return map.get(key).toString();
            }
        }

        // 第三步，获取requestBody
        RequestWrapper requestWrapper = new RequestWrapper(request);
        String body = requestWrapper.getBody();
        if(StringUtils.isNotBlank(body)){
            Map<String,Object> bodyMap = JSONObject.parseObject(body, new TypeReference<Map<String,Object>>(){});
            if (bodyMap.containsKey(key)){
                return bodyMap.get(key).toString();
            }
        }
        return null;
    }


    /**
     * 返回异常信息
     * @param response 响应
     */
    private void returnMsg(HttpServletResponse response,ErrorCodeEnum requestTooFrequent,Class<?> returnType){
        Object richResult = getResult(returnType,requestTooFrequent);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        String jsonObject = JSONObject.toJSONString(richResult);
        try {
            out = response.getWriter();
            out.append(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 组合特定的返回对象
     * @param returnType 返回值类型
     * @param requestTooFrequent 错误原因
     * @return 返回值
     */
    private Object getResult(Class<?> returnType,ErrorCodeEnum requestTooFrequent){
        return new DataResult<Map<String, Object>>(requestTooFrequent.getHttpCode(),requestTooFrequent.getInternalErrorCode(),requestTooFrequent.getErrorMessage());
    }
}
