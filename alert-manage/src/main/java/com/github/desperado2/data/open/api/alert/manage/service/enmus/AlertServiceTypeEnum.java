package com.github.desperado2.data.open.api.alert.manage.service.enmus;

/**
 * 告警类型枚举
 * @author tu nan
 * @date 2021/3/11
 **/
public enum AlertServiceTypeEnum {

    /**
     * 钉钉
     */
    DINGDING("DINGDING","钉钉机器人", "dingDingAlertServiceImpl"),

    /**
     * 日志
     */
    LOG("LOG","日志", "logAlertServiceImpl");


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

    AlertServiceTypeEnum(String code, String name, String executorBeanName) {
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
