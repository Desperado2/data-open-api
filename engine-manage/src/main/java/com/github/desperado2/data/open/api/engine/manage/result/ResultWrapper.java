package com.github.desperado2.data.open.api.engine.manage.result;

import com.github.desperado2.data.open.api.common.manage.enums.ExternalResultCodeEnum;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 结果包装类
 */

public class ResultWrapper {
    @ApiModelProperty(value = "结果码")
    private String returnCode;
    @ApiModelProperty(value = "成功或者失败的描述")
    private String message;
    @ApiModelProperty(value = "返回的业务数据，如没有为null")
    private Object results;
    @ApiModelProperty(value = "返回的业务数据数据量，如没有为0")
    private Integer dataSize;

    public ResultWrapper() {}


    public ResultWrapper(String returnCode, String message, Object results) {
        this.returnCode = returnCode;
        this.message = message;
        this.results = results;
        if(results instanceof List){
            this.dataSize = ((List<?>) this.results).size();
        }else if(this.results == null){
            this.dataSize = 0;
        }else{
            this.dataSize = 1;
        }
    }

    public ResultWrapper(Object results) {
        this(ExternalResultCodeEnum.SUCCESS.getCode(), ExternalResultCodeEnum.SUCCESS.getName(), results);
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResults() {
        return results;
    }

    public void setResults(Object results) {
        this.results = results;
    }

    public Integer getDataSize() {
        return dataSize;
    }

    public void setDataSize(Integer dataSize) {
        this.dataSize = dataSize;
    }
}
