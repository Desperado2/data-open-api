package com.github.desperado2.data.open.api.engine.manage.interceptor;

public interface ISQLInterceptor {
    String before(String script);

    void after(String script);
}
