package com.github.desperado2.data.open.api.engine.manage.result;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 执行结果返回
 */

public interface IResultWrapper {


    public Object wrapper(Object data, HttpServletRequest request, HttpServletResponse response, JSONObject responseFormat);

    public Object throwable(Throwable throwable,HttpServletRequest request, HttpServletResponse response, JSONObject responseFormat);

    public Object exception(String returnCode, String message,HttpServletRequest request, HttpServletResponse response, JSONObject responseFormat);
}
