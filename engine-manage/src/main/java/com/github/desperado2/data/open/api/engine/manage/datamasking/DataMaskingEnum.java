package com.github.desperado2.data.open.api.engine.manage.datamasking;



/**
 * 数据脱敏方式枚举类型
 * @author tu nan
 * @date 2023/7/6
 **/
public enum DataMaskingEnum {
    /**
     * 字符覆盖
     */
    CHARACTER_MASKING("CharacterMasking","groovy类型脚本", "characterMaskingService");


    /**
     * 脱敏类型码
     */
    private String code;

    /**
     * 脱敏类型名称
     */
    private String name;


    /**
     * 执行器bean名称
     */
    private String executorBeanName;

    DataMaskingEnum(String code, String name, String executorBeanName) {
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

    public  String getExecutorBeanName() {
        return executorBeanName;
    }

    public static DataMaskingEnum getExecutorBeanNameByCode(String code) {
        for (DataMaskingEnum value : DataMaskingEnum.values()) {
            if(value.code.equals(code)){
                return value;
            }
        }
        return null;
    }
}
