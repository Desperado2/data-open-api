package com.github.desperado2.data.open.api.engine.manage.model;

/**
 * 取消返回结构体的封装
 */
public class IgnoreWrapper {
    private Object data;

    public IgnoreWrapper() {
    }

    public IgnoreWrapper(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
