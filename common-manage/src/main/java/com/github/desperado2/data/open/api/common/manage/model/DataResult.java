package com.github.desperado2.data.open.api.common.manage.model;

import com.github.desperado2.data.open.api.common.manage.enums.ErrorCodeEnum;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 结果集
 *
 * @author tu nan
 * @date 2023/2/7
 **/
@ApiModel(value = "DataResult", description = "结果数据集")
public class DataResult<T> {

    @ApiModelProperty("数据")
    private T data;

    @ApiModelProperty("状态码")
    private Integer statusCode;

    @ApiModelProperty("提示信息")
    private String msg;

    @ApiModelProperty("错误码")
    private String internalErrorCode;

    public DataResult() {
        this.internalErrorCode = "0";
        this.msg = "success";
        this.statusCode = 200;
    }

    public DataResult(T results) {
        this();
        this.setStatusCode(200);
        this.data = results;
    }



    public DataResult(Integer errorCode, String errorMsg) {
        this.internalErrorCode = "0";
        this.msg = "success";
        this.statusCode = errorCode;
        this.msg = errorMsg;
    }

    public DataResult(Integer state, String internalErrorCode, String errorMsg) {
        this.internalErrorCode = "0";
        this.msg = "success";
        this.statusCode = state;
        this.internalErrorCode = internalErrorCode;
        this.msg = errorMsg;
    }

    public DataResult(ErrorCodeEnum baseErrorCode) {
        this.internalErrorCode = "0";
        this.msg = "success";
        this.statusCode = baseErrorCode.getHttpCode();
        this.internalErrorCode = baseErrorCode.getInternalErrorCode();
        this.msg = baseErrorCode.getErrorMessage();
    }

    public DataResult(Throwable e) {
        this();
        if (e instanceof DataOpenPlatformException) {
            this.setMsg(((DataOpenPlatformException)e).getErrorMessage());
            this.setStatusCode(((DataOpenPlatformException)e).getStatusCode());
        } else {
            this.setMsg("未知异常");
            this.setStatusCode(500);
        }

    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getInternalErrorCode() {
        return internalErrorCode;
    }

    public void setInternalErrorCode(String internalErrorCode) {
        this.internalErrorCode = internalErrorCode;
    }
}
