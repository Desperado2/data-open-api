package com.github.desperado2.data.open.api.engine.manage.interceptor;

import org.springframework.stereotype.Component;

@Component
public class DefaultSQLInterceptor implements ISQLInterceptor {

    @Override
    public String before(String script) {
        return script;
    }

    @Override
    public void after(String script) {
    }
}
