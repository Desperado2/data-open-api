package com.github.desperado2.data.open.api.datasource.manage.model;

/**
 * 字段描述
 */
public class FieldInfo {
    /**
     * 字段名
     */
    private String name;
    /**
     * 注释
     */
    private String comment;
    /**
     * 类型
     */
    private String type;

    public FieldInfo() {
    }

    public FieldInfo(String name, String comment, String type) {
        this.name = name;
        this.comment = comment;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
