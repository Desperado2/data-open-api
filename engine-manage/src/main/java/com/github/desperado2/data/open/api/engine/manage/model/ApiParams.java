package com.github.desperado2.data.open.api.engine.manage.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * api 接口入参实体
 */

public class ApiParams {
    private Map<String,String> pathVar;
    private Map<String,Object> param;
    private Map<String,Object> body;
    private Map<String,String> header;
    private Map<String,Object> cookie;
    private Map<String,Object> session;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public ApiParams() {
    }

    public ApiParams(Map<String, String> pathVar, Map<String, Object> param, Map<String, Object> body, Map<String, String> header, Map<String, Object> cookie, Map<String, Object> session, HttpServletRequest request, HttpServletResponse response) {
        this.pathVar = pathVar;
        this.param = param;
        this.body = body;
        this.header = header;
        this.cookie = cookie;
        this.session = session;
        this.request = request;
        this.response = response;
    }

    private ApiParams(Builder builder) {
        setPathVar(builder.pathVar);
        setParam(builder.param);
        setBody(builder.body);
        setHeader(builder.header);
        setCookie(builder.cookie);
        setSession(builder.session);
        setRequest(builder.request);
        setResponse(builder.response);
    }

    public ApiParams putParam(String key, Object value){
        if (param == null){
            param = new HashMap<>();
        }
        param.put(key,value);
        return this;
    }

    public Map<String, String> getPathVar() {
        return pathVar;
    }

    public void setPathVar(Map<String, String> pathVar) {
        this.pathVar = pathVar;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }

    public Map<String, Object> getBody() {
        return body;
    }

    public void setBody(Map<String, Object> body) {
        this.body = body;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public Map<String, Object> getCookie() {
        return cookie;
    }

    public void setCookie(Map<String, Object> cookie) {
        this.cookie = cookie;
    }

    public Map<String, Object> getSession() {
        return session;
    }

    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public static  Builder builder(){
        return new Builder();
    }

    public static final class Builder {
        private Map<String, String> pathVar;
        private Map<String, Object> param;
        private Map<String, Object> body;
        private Map<String, String> header;
        private Map<String, Object> cookie;
        private Map<String, Object> session;
        private HttpServletRequest request;
        private HttpServletResponse response;

        public Builder() {
        }

        public Builder pathVar(Map<String, String> val) {
            pathVar = val;
            return this;
        }

        public Builder param(Map<String, Object> val) {
            param = val;
            return this;
        }

        public Builder body(Map<String, Object> val) {
            body = val;
            return this;
        }

        public Builder header(Map<String, String> val) {
            header = val;
            return this;
        }

        public Builder cookie(Map<String, Object> val) {
            cookie = val;
            return this;
        }

        public Builder session(Map<String, Object> val) {
            session = val;
            return this;
        }

        public Builder request(HttpServletRequest val) {
            request = val;
            return this;
        }

        public Builder response(HttpServletResponse val) {
            response = val;
            return this;
        }

        public ApiParams build() {
            return new ApiParams(this);
        }
    }
}
