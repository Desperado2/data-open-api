package com.github.desperado2.data.open.api.common.manage.exception;

/**
 * 对外返回异常
 * @author tu nan
 * @date 2021/3/11
 **/
public class ExternalException extends Exception {
    private String apiPath;
    private String method;
    private String returnCode;
    private String message;

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ExternalException(String returnCode, String message, String apiPath, String method) {
        super(message);
        this.returnCode = returnCode;
        this.message = message;
    }

    public String getApiPath() {
        return apiPath;
    }

    public void setApiPath(String apiPath) {
        this.apiPath = apiPath;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public ExternalException(String message,String apiPath, String method, Throwable cause) {
        super(message, cause);
    }
}
