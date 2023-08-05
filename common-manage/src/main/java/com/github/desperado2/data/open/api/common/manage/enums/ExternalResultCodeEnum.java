package com.github.desperado2.data.open.api.common.manage.enums;

/**
 * 返回结果状态码
 * @author tu nan
 * @date 2021/3/11
 **/
public enum ExternalResultCodeEnum {

    /**
     * 请求成功
     */
    SUCCESS("E00000","请求成功"),

    /**
     * 请求失败
     */
    FAIL("E99999","请求失败"),

    /**
     * 参数错误
     */
    PARAM_ERROR("E01001","参数错误"),

    /**
     * 签名错误
     */
    SIGN_ERROR("E01002","签名错误"),

    /**
     * appKey错误
     */
    APPKEY_ERROR("E01003","appKey错误"),

    /**
     * 签名已过期
     */
    SIGN_EXPIRE("E01004","签名已过期"),

    /**
     * 请求时间戳不合法
     */
    TIMESTAMP_NOT_LICIT("E01005","请求时间戳不合法"),


    FORBID("E01006","您没有权限调用本接口"),

    API_NOT_EXISTS("E01007","请求API不存在"),


    /**
     * 业务异常
     */
    BUSINESS_ERROR("E02001","业务异常"),

    /**
     * 未知的数据源
     */
    UNKNOWN_DATASOURCE("E02002","未知的数据源");

    /**
     * 返回结果状态码
     */
    private String code;

    /**
     * 返回结果状态名称
     */
    private String name;

    ExternalResultCodeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
