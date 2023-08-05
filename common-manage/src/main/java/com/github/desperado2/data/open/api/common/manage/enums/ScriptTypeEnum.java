package com.github.desperado2.data.open.api.common.manage.enums;

/**
 * 脚本类型枚举
 * @author tu nan
 * @date 2021/3/11
 **/
public enum ScriptTypeEnum {

    /**
     * groovy
     */
    GROOVY("GROOVY","groovy类型脚本", "groovyScriptExecutor"),

    /**
     * Mybatis SQL
     */
    SQL("SQL","Mybatis SQL类型脚本", "mybatisScriptExecutor"),

    /**
     * javascript
     */
    JAVASCRIPT("JAVASCRIPT","Mybatis SQL类型脚本", "javaScriptScriptExecutor");

    /**
     * 限流类型码
     */
    private String code;

    /**
     * 限流类型名称
     */
    private String name;


    /**
     * 执行器bean名称
     */
    private String executorBeanName;

    ScriptTypeEnum(String code, String name, String executorBeanName) {
        this.code = code;
        this.name = name;
        this.executorBeanName = executorBeanName;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getExecutorBeanName() {
        return executorBeanName;
    }
}
