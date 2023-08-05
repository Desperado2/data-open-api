package com.github.desperado2.data.open.api.engine.manage.model;

import java.util.Arrays;

/**
 * 脱敏实体
 */

public class MaskParams {
    private String name;
    private Object[] param;

    public MaskParams() {
    }

    public MaskParams(String name, Object[] param) {
        this.name = name;
        this.param = param;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object[] getParam() {
        return param;
    }

    public void setParam(Object[] param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return "MaskParams{" +
                "name='" + name + '\'' +
                ", param=" + Arrays.toString(param) +
                '}';
    }
}
