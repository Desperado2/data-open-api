package com.github.desperado2.data.open.api.engine.manage.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.github.desperado2.data.open.api.cache.manage.chche.IApiInfoCache;
import com.github.desperado2.data.open.api.cache.manage.model.ApiInfo;
import com.github.desperado2.data.open.api.common.manage.config.OpenApiProperties;
import com.github.desperado2.data.open.api.common.manage.constants.Constants;
import com.github.desperado2.data.open.api.common.manage.enums.ApiExecuteEnvironmentEnum;
import com.github.desperado2.data.open.api.common.manage.enums.ScriptTypeEnum;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.utils.RequestUtils;
import com.github.desperado2.data.open.api.engine.manage.ApiInfoContent;
import com.github.desperado2.data.open.api.engine.manage.encrypt.IScriptEncrypt;
import com.github.desperado2.data.open.api.engine.manage.enums.ExecuteType;
import com.github.desperado2.data.open.api.engine.manage.model.ApiParams;
import com.github.desperado2.data.open.api.engine.manage.model.IgnoreWrapper;
import com.github.desperado2.data.open.api.engine.manage.result.IResultWrapper;
import com.github.desperado2.data.open.api.engine.manage.scripts.ScriptExecutor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 将存储的API注册为request mapping，并且提供对入参及存储的执行脚本进行解析。
 * 输出解析后的最终脚本提供给脚本执行器，然后对结果进行封装返回
 * @author mujingjing
 * @date 2023/3/13
 **/
@Component
public class QLRequestMappingFactory {

    private final static Logger logger = LoggerFactory.getLogger(QLRequestMappingFactory.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ScriptExecutor scriptExecutor;

    @Autowired
    private OpenApiProperties openApiProperties;

    @Autowired
    private IApiInfoCache apiInfoCache;

    @Autowired
    private ApiInfoContent apiInfoContent;

    @Autowired
    private IResultWrapper resultWrapper;

    @Autowired
    private IScriptEncrypt scriptEncrypt;


    private List<String> bodyMethods = Arrays.asList("POST", "PUT", "PATCH");


    @RequestMapping
    @ResponseBody
    public ResponseEntity execute(@PathVariable(required = false) Map<String, String> pathVar,
                                  @RequestParam(required = false) Map<String, Object> param,
                                  HttpServletRequest request, HttpServletResponse response) throws Throwable {
        String pattern = RequestUtils.buildPattern(request);
        String method = request.getMethod();
        HashMap<String, Object> body = new HashMap<>();
        String executeEnv = request.getHeader(Constants.API_REQUEST_ENVIRONMENT);
        if(bodyMethods.contains(method)){
            if(request.getContentType() != null && request.getContentType().contains("application/json")){
                try {
                    Object bodyObject = objectMapper.readValue(request.getInputStream(), Object.class);
                    if(bodyObject instanceof Map){
                        body.putAll((Map<? extends String, ?>)bodyObject);
                    }
                    body.put(openApiProperties.getBodyRootKey(), bodyObject);
                }catch (MismatchedInputException exception){
                    throw new HttpMessageNotReadableException("Required request body is missing", exception, new ServletServerHttpRequest(request));
                }
            }else if(request.getContentType() != null && request.getContentType().contains("multipart/form-data")){
                MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
                body.putAll(multipartHttpServletRequest.getMultiFileMap());
                body.put(openApiProperties.getBodyRootKey(), multipartHttpServletRequest.getMultiFileMap());
            }else if(request.getContentType() != null && request.getContentType().contains("application/x-www-form-urlencoded")){
                Map<String, List<Object>> parameterMap = new HashMap<>(request.getParameterMap().size());
                request.getParameterMap().forEach((key, value) -> {
                    parameterMap.put(key,Arrays.asList(value));
                });
                body.putAll(parameterMap);
                body.put(openApiProperties.getBodyRootKey(), parameterMap);
            }
        }
        ApiParams apiParams = ApiParams.builder().pathVar(pathVar)
                .header(RequestUtils.buildHeaderParams(request))
                .param(param)
                .body(body)
                .session(RequestUtils.buildSessionParams(request))
                .request(request)
                .response(response)
                .build();

        // 获取API信息  执行
        ApiInfo apiInfo = apiInfoCache.get(ApiInfo.Builder.builder().method(method).path(pattern).build());
        String script = scriptEncrypt.decrypt(apiInfo.getScript());
        try {
            Object data = null;
            ApiExecuteEnvironmentEnum environment = getEnvironmentByPath(executeEnv);
            if(ScriptTypeEnum.GROOVY.getCode().equals(apiInfo.getType())){
                data = scriptExecutor.getScriptExecutor(ScriptTypeEnum.GROOVY).runScript(ExecuteType.EXTERNAL,environment, script, apiInfo, apiParams);
            }else if(ScriptTypeEnum.SQL.getCode().equals(apiInfo.getType())){
                data = scriptExecutor.getScriptExecutor(ScriptTypeEnum.SQL).runScript(ExecuteType.EXTERNAL, environment, script, apiInfo, apiParams);
            }else if(ScriptTypeEnum.JAVASCRIPT.getCode().equals(apiInfo.getType())){
                data = scriptExecutor.getScriptExecutor(ScriptTypeEnum.JAVASCRIPT).runScript(ExecuteType.EXTERNAL, environment, script, apiInfo, apiParams);
            }else{
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(resultWrapper.throwable(new Throwable("无效的执行引擎"), request, response, apiInfo.getResponseFormat()));
            }
            if(data instanceof ResponseEntity){
                return (ResponseEntity) data;
            }
            if(data instanceof IgnoreWrapper){
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(((IgnoreWrapper)data).getData());
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(resultWrapper.wrapper(data, request, response, apiInfo.getResponseFormat()));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(resultWrapper.throwable(e, request, response, apiInfo.getResponseFormat()));
        }finally {
            apiInfoContent.removeAll();
        }
    }


    private ApiExecuteEnvironmentEnum getEnvironmentByPath(String executeEnv) throws DataOpenPlatformException {
        if(StringUtils.isBlank(executeEnv)){
            // 未指定默认为测试
            return ApiExecuteEnvironmentEnum.TEST;
            //throw new DataOpenPlatformException("未知的环境，executeEnv=" + executeEnv);
        }
        if(ApiExecuteEnvironmentEnum.TEST.getCode().equals(executeEnv.toLowerCase().trim())){
            return ApiExecuteEnvironmentEnum.TEST;
        }
        if(ApiExecuteEnvironmentEnum.PRE.getCode().equals(executeEnv.toLowerCase().trim())){
            return ApiExecuteEnvironmentEnum.PRE;
        }
        if(ApiExecuteEnvironmentEnum.PROD.getCode().equals(executeEnv.toLowerCase().trim())){
            return ApiExecuteEnvironmentEnum.PROD;
        }
        throw new DataOpenPlatformException("未知的环境，executeEnv=" + executeEnv);
    }

}
