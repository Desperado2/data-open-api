package com.github.desperado2.data.open.api.api.manage.enums;


/**
 * API状态
 * @author tu nan
 * @date 2021/3/10
 **/
public enum ApiStatusEnum {

    /**
     * 下架
     */
    DOWN(0,"全下架"),

    /**
     * 只上架正式 001
     */
    UP_PROD(1,"只上架正式"),

    /**
     * 只上架预发布  010
     */
    UP_PRE(2,"只上架预发布"),

    /**
     * 上架预发布和正式  011
     */
    UP_PRE_AND_PROD(3,"上架预发布和正式"),

    /**
     * 只上架测试  100
     */
    UP_TEST(4,"只上架测试"),

    /**
     * 上架测试和正式  101
     */
    UP_TEST_AND_PROD(5,"上架测试和正式"),

    /**
     * 上架测试和预发 110
     */
    UP_TEST_AND_PRE(6,"上架测试和预发"),

    /**
     * 上级全部  111
     */
    UP(7,"全上架");


    /**
     * 状态码
     */
    private Integer code;

    /**
     * 状态名称
     */
    private String name;

    ApiStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static boolean isExist(Integer code){
        for (ApiStatusEnum value : values()) {
            if(value.code.equals(code)){
                return true;
            }
        }
        return false;
    }
}
