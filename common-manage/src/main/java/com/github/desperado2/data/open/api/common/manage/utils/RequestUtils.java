package com.github.desperado2.data.open.api.common.manage.utils;

import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求工具类
 */
public class RequestUtils {

    public static String buildPattern(HttpServletRequest request) {
        String attribute = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        return attribute.startsWith("/") ? attribute : "/" + attribute;
    }

    public static Map<String, Object> buildSessionParams(HttpServletRequest request) {
        Enumeration<String> keys = request.getSession().getAttributeNames();
        Map<String,Object> result = new HashMap<>();
        while (keys.hasMoreElements()){
            String key = keys.nextElement();
            result.put(key,request.getSession().getAttribute(key));
        }
        return result;
    }

    public static  Map<String, String> buildHeaderParams(HttpServletRequest request) throws UnsupportedEncodingException {
        Enumeration<String> headerKeys = request.getHeaderNames();
        Map<String, String> result  = new HashMap<>();
        while (headerKeys.hasMoreElements()){
            String key = headerKeys.nextElement();
            String value = request.getHeader(key);
            result.put(key, value);
        }
        return result;
    }
}
