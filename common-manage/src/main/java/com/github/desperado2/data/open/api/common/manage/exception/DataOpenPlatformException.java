package com.github.desperado2.data.open.api.common.manage.exception;


/**
 * 平台全局异常
 * @author tu nan
 * @date 2021/3/23
 **/
public class DataOpenPlatformException extends Exception  {

    private int statusCode;
    private String errorMessage;
    private String internalErrorCode;


    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getInternalErrorCode() {
        return internalErrorCode;
    }

    public void setInternalErrorCode(String internalErrorCode) {
        this.internalErrorCode = internalErrorCode;
    }

    public DataOpenPlatformException() {
    }

    public DataOpenPlatformException(String message) {
        super(message);
    }

    public DataOpenPlatformException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public DataOpenPlatformException(String message, String errorCode) {
        super(message);
        this.internalErrorCode = errorCode;
    }

    public DataOpenPlatformException(String message, Throwable cause) {
        super(message, cause);
    }


}
