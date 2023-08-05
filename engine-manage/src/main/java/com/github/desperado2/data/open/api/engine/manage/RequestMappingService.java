package com.github.desperado2.data.open.api.engine.manage;


import com.github.desperado2.data.open.api.cache.manage.model.ApiInfo;
import com.github.desperado2.data.open.api.common.manage.config.OpenApiProperties;
import com.github.desperado2.data.open.api.engine.manage.config.QLRequestMappingFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Request注册
 * @author tu nan
 * @date 2023/3/13
 **/
@Service
public class RequestMappingService {

    private static final Logger logger = LoggerFactory.getLogger(RequestMappingService.class);

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Autowired
    private OpenApiProperties openApiProperties;

    @Autowired
    @Lazy
    private QLRequestMappingFactory mappingFactory;


    /**
     * 获取已经注册的API
     * @return API列表
     */
    public List<ApiInfo> getPathListForCode(){
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        List<ApiInfo> result = new ArrayList<>(handlerMethods.size());
        for (RequestMappingInfo info : handlerMethods.keySet()) {
            if(handlerMethods.get(info).getMethod().getDeclaringClass() == QLRequestMappingFactory.class){
                continue;
            }
            String groupName = handlerMethods.get(info).getBeanType().getSimpleName();
            for (String path : info.getPatternsCondition().getPatterns()) {
                // 过滤本身的类
                if(path.indexOf(openApiProperties.getBaseRegisterPath()) == 0 || path.equals("/error")){
                    continue;
                }
                Set<RequestMethod> methods = info.getMethodsCondition().getMethods();
                if(methods.isEmpty()){
                    result.add(ApiInfo.Builder.builder().path(path).method("ALL").build());
                }else{
                    for (RequestMethod method : methods) {
                        result.add(ApiInfo.Builder.builder().path(path).method(method.name()).build());
                    }
                }
            }
        }
        return result;
    }

    /**
     * 注册mapping
     * @param openApiDetails 详情
     * @throws NoSuchMethodException
     */
    public synchronized void registerMappingForApiInfo(ApiInfo openApiDetails) throws NoSuchMethodException {
        String apiAddress = openApiDetails.getPath();
        String method = openApiDetails.getMethod();
        if(StringUtils.isBlank(apiAddress) || StringUtils.isBlank(method)){
            return;
        }
        apiAddress = openApiProperties.getBaseRegisterPath() + apiAddress;
        method = method.toUpperCase(Locale.ROOT);
        RequestMappingInfo mappingInfo = getRequestMappingInfo(apiAddress, method);
        if(mappingInfo != null){
            return;
        }
        logger.info("Mapped [{}]{}", method, apiAddress);
        PatternsRequestCondition patternsRequestCondition = new PatternsRequestCondition(apiAddress);
        RequestMethodsRequestCondition requestMethodsRequestCondition = new RequestMethodsRequestCondition(RequestMethod.valueOf(method));
        mappingInfo = new RequestMappingInfo(patternsRequestCondition, requestMethodsRequestCondition,null, null,null,null,null);
        Method targetMethod = QLRequestMappingFactory.class.getDeclaredMethod("execute", Map.class, Map.class, HttpServletRequest.class, HttpServletResponse.class);
        requestMappingHandlerMapping.registerMapping(mappingInfo, mappingFactory,targetMethod);
    }

    /**
     * 取消注册的mapping
     * @param openApiDetails 详情
     * @throws NoSuchMethodException
     */
    public synchronized void unregisterMappingForApiInfo(ApiInfo openApiDetails) throws NoSuchMethodException {
        String apiAddress = openApiDetails.getPath();
        String method = openApiDetails.getMethod();
        if(StringUtils.isBlank(apiAddress) || StringUtils.isBlank(method)){
            return;
        }
        apiAddress = openApiProperties.getBaseRegisterPath() + apiAddress;
        openApiDetails.setPath(apiAddress);
        method = method.toUpperCase(Locale.ROOT);
        openApiDetails.setMethod(method);
        RequestMappingInfo mappingInfo = getRequestMappingInfo(apiAddress, method);
        if(mappingInfo == null){
            return;
        }
        logger.debug("Cancel Mapped [{}]{}", openApiDetails.getMethod(), apiAddress);
        PatternsRequestCondition patternsRequestCondition = new PatternsRequestCondition(apiAddress);
        RequestMethodsRequestCondition requestMethodsRequestCondition = new RequestMethodsRequestCondition(RequestMethod.valueOf(method));
        mappingInfo = new RequestMappingInfo(patternsRequestCondition, requestMethodsRequestCondition,null, null,null,null,null);
        requestMappingHandlerMapping.unregisterMapping(mappingInfo);
    }

    /**
     * 判断是否是原始代码注册的Mapping
     * @param pattern 路径
     * @param method 请求方式
     * @return 是true  否false
     */
    public Boolean isCodeMapping(String pattern, String method){
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        for (RequestMappingInfo requestMappingInfo : handlerMethods.keySet()) {
            if(handlerMethods.get(requestMappingInfo).getMethod().getDeclaringClass() == QLRequestMappingFactory.class){
                continue;
            }
            Set<String> patterns = requestMappingInfo.getPatternsCondition().getPatterns();
            Set<RequestMethod> methods = requestMappingInfo.getMethodsCondition().getMethods();
            if(patterns.contains(pattern) && (methods.isEmpty() || methods.contains(RequestMethod.valueOf(method)))){
                return true;
            }
        }
        return false;
    }

    private RequestMappingInfo getRequestMappingInfo(String pattern, String method){
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        for (RequestMappingInfo info : handlerMethods.keySet()) {
            Set<String> patterns = info.getPatternsCondition().getPatterns();
            Set<RequestMethod> methods = info.getMethodsCondition().getMethods();
            if(patterns.contains(pattern) && (methods.isEmpty() || methods.contains(RequestMethod.valueOf(method)))){
                return info;
            }
        }
        return null;
    }
}
