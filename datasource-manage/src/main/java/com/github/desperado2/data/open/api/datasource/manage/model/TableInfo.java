package com.github.desperado2.data.open.api.datasource.manage.model;


import java.util.List;

/**
 * 表信息描述
 */

public class TableInfo {
    /**
     * 表名
     */
    private String name;
    /**
     * 注释
     */
    private String comment;
    /**
     * 字段描述
     */
    List<FieldInfo> fields;

    public TableInfo() {
    }

    public TableInfo(String name, String comment, List<FieldInfo> fields) {
        this.name = name;
        this.comment = comment;
        this.fields = fields;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<FieldInfo> getFields() {
        return fields;
    }

    public void setFields(List<FieldInfo> fields) {
        this.fields = fields;
    }
}
