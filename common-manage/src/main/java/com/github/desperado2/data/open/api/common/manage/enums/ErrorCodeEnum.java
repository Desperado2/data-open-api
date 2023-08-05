package com.github.desperado2.data.open.api.common.manage.enums;



/**
 * @Description:    错误码设置: 9000001分成三段 9 000 001，分别表示：错误级别(9=系统级, 2=服务级)，微服务占用端口号，内部错误码
 * @Author:         xiaoliang.chen
 * @Date:     2019/2/21 上午11:44
 */
public enum ErrorCodeEnum {
    /** 系统异常 */
    SYSTEM_ERROR(500 , "9000001" , "系统异常"),

    /** 服务异常 */
    SERVICE_ERROR(500 , "2000001" , "服务异常"),

    /** 参数异常 */
    PARAMS_ERROR(500 , "2000103" , "参数{}不正确"),
    PARAMS_MISSING(500 , "2000105" , "参数丢失"),
    PARAMS_READABLE(500 , "2000106" , "could_not_read_json"),
    PARAMS_BIND(500 , "2000107" , "参数绑定失败"),
    PARAMS_CONSTRAINT(500 , "2000108" , "参数不符合约束条件"),
    PARAMS_VALIDATION(500 , "2000109" , "参数验证失败"),
    PARAMS_FORMAT(500 , "2000110" , "参数格式化失败"),
    ID_VALUE_INVALID(500 , "2000104" , "参数id的值非法"),
    ACCESS_FORBIDDEN (403 , "2000111" , "无权访问"),
    VALID_STATUS (500 , "2000112" , "无效的状态"),
    RESOURCE_NOT_EXISTS (500 , "2000113" , "访问资源不存在"),

    /** 请求异常 */
    REQUEST_NOT_ALLOW(405,"2000201","不支持当前请求方法"),
    UNSUPPORTED_MEDIA_TYPE(415,"2000202","不支持当前媒体类型"),


    /**
     * 限流异常
     */
    REQUEST_TOO_FREQUENT_CODE(500,"2000501","访问过于频繁，请稍后再试"),
    REQUEST_TOO_FREQUENT_IP(500,"2000502","访问过于频繁，请稍后再试"),
    REQUEST_TOO_FREQUENT_PHONE(500,"2000503","访问过于频繁，请稍后再试"),

    /**
     * 登录类异常
     */
    LOGIN_NEED_IDENTIFYING_CODE(100,"2000601","帐号密码错误超过最大次数");


    private int code;
    private String msg;
    private String internalCode;

    private ErrorCodeEnum(int code, String internalCode, String msg){
        this.code = code;
        this.msg = msg;
        this.internalCode = internalCode;
    }

    public int getHttpCode() {
        return code;
    }

    public String getErrorMessage() {
        return msg;
    }

    public String getInternalErrorCode() {
        return internalCode;
    }
}
