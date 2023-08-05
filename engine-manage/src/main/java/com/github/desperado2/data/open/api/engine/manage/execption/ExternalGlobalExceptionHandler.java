package com.github.desperado2.data.open.api.engine.manage.execption;


import com.github.desperado2.data.open.api.cache.manage.chche.IApiInfoCache;
import com.github.desperado2.data.open.api.cache.manage.model.ApiInfo;
import com.github.desperado2.data.open.api.common.manage.exception.ExternalException;
import com.github.desperado2.data.open.api.engine.manage.result.IResultWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 功能描述: 全局异常接收
 * @author corbett
 * @date 2018/9/6 13:41
 */
@RestControllerAdvice
public class ExternalGlobalExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(ExternalGlobalExceptionHandler.class);

    @Autowired
    private IApiInfoCache apiInfoCache;

    @Autowired
    private IResultWrapper resultWrapper;

    /**
     * 对外接口自定义异常
     * @param e 异常
     * @return 返回值
     */
    @ExceptionHandler(ExternalException.class)
    public ResponseEntity externalException(ExternalException e) {

        logger.info("对外接口自义定异={}" , e.getClass().getName());
        e.printStackTrace();
        ApiInfo apiInfo = apiInfoCache.get(ApiInfo.Builder.builder().path(e.getApiPath()).method(e.getMethod()).build());
        if(apiInfo == null){
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(resultWrapper.exception(e.getReturnCode(), e.getMessage(), null, null, null));
        }
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(resultWrapper.exception(e.getReturnCode(), e.getMessage(), null, null, apiInfo.getResponseFormat()));
    }

}
