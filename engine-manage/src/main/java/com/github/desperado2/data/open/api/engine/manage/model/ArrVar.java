package com.github.desperado2.data.open.api.engine.manage.model;

/**
 * 数组变量对象
 */

public class ArrVar {
    private String varName;
    private Integer index;

    public ArrVar(String varName, Integer index) {
        this.varName = varName;
        this.index = index;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
