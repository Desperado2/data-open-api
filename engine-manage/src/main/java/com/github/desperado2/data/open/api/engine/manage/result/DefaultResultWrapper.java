package com.github.desperado2.data.open.api.engine.manage.result;

import com.alibaba.fastjson.JSONObject;
import com.github.desperado2.data.open.api.common.manage.enums.ExternalResultCodeEnum;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 默认结果包装类
 */
@Component
public class DefaultResultWrapper implements IResultWrapper {

    @Override
    public Object wrapper(Object data, HttpServletRequest request, HttpServletResponse response, JSONObject responseFormat) {
        ResultWrapper resultWrapper = new ResultWrapper(data);
        if(responseFormat == null){
            return resultWrapper;
        }
        return ReturnValueTransform.parseReturnValue(responseFormat, resultWrapper);
    }

    @Override
    public Object throwable(Throwable throwable, HttpServletRequest request, HttpServletResponse response, JSONObject responseFormat) {
        ResultWrapper resultWrapper = new ResultWrapper(ExternalResultCodeEnum.BUSINESS_ERROR.getCode(), throwable.getMessage(), null);
        if(responseFormat == null){
            return resultWrapper;
        }
        return ReturnValueTransform.parseReturnValue(responseFormat, resultWrapper);
    }

    @Override
    public Object exception(String returnCode, String message, HttpServletRequest request, HttpServletResponse response, JSONObject responseFormat) {
        ResultWrapper resultWrapper = new ResultWrapper(returnCode, message, null);
        if(responseFormat == null){
            return resultWrapper;
        }
        return ReturnValueTransform.parseReturnValue(responseFormat, resultWrapper);
    }
}
