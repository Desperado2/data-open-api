package com.github.desperado2.data.open.api.datasource.manage.model;


import com.github.desperado2.data.open.api.datasource.manage.datasource.DataSourceDialect;

import java.util.LinkedHashMap;


public class ScriptContext {

    /**
     * 执行脚本
     */
    private StringBuilder script;

    /**
     * 绑定参数
     */
    LinkedHashMap<String, Object> bindParams;

    /**
     * 执行脚本数据源
     */
    private DataSourceDialect dataSourceDialect;

    /**
     * ES索引
     */
    private String esIndex;

    public ScriptContext() {
    }

    public ScriptContext(StringBuilder script, DataSourceDialect dataSourceDialect) {
        this.script = script;
        this.dataSourceDialect = dataSourceDialect;
    }

    public ScriptContext(StringBuilder script,  LinkedHashMap<String, Object> bindParams, DataSourceDialect dataSourceDialect) {
        this.script = script;
        this.bindParams = bindParams;
        this.dataSourceDialect = dataSourceDialect;
    }

    public ScriptContext(StringBuilder script,  DataSourceDialect dataSourceDialect, String esIndex) {
        this.script = script;
        this.dataSourceDialect = dataSourceDialect;
        this.esIndex = esIndex;
    }

    public ScriptContext(StringBuilder script,  LinkedHashMap<String, Object> bindParams, DataSourceDialect dataSourceDialect, String esIndex) {
        this.script = script;
        this.bindParams = bindParams;
        this.dataSourceDialect = dataSourceDialect;
        this.esIndex = esIndex;
    }

    public StringBuilder getScript() {
        return script;
    }

    public void setScript(StringBuilder script) {
        this.script = script;
    }

    public LinkedHashMap<String, Object> getBindParams() {
        return bindParams;
    }

    public void setBindParams(LinkedHashMap<String, Object> bindParams) {
        this.bindParams = bindParams;
    }

    public DataSourceDialect getDataSourceDialect() {
        return dataSourceDialect;
    }

    public void setDataSourceDialect(DataSourceDialect dataSourceDialect) {
        this.dataSourceDialect = dataSourceDialect;
    }

    public String getEsIndex() {
        return esIndex;
    }

    public void setEsIndex(String esIndex) {
        this.esIndex = esIndex;
    }
}
