package com.github.desperado2.data.open.api.common.manage.model;

import com.github.desperado2.data.open.api.common.manage.enums.ExternalResultCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 外部接口返回结果
 * @author tu nan
 * @date 2021/3/11
 **/
@ApiModel(description = "外部接口返回结果")
public class ExternalResult<T>{


	@ApiModelProperty(value = "返回成功标识：200-成功；非200-失败")
	private Integer state;
	@ApiModelProperty(value = "结果码")
	private String returnCode;
	@ApiModelProperty(value = "成功或者失败的描述")
	private String message;
	@ApiModelProperty(value = "返回的业务数据，如没有为null")
	private List<T> results;
	@ApiModelProperty(value = "返回的业务数据数据量，如没有为0")
	private Integer dataSize;

	public ExternalResult() {}


	public ExternalResult(Integer state, String returnCode, String message, List<T> results) {
		this.state = state;
		this.returnCode = returnCode;
		this.message = message;
		this.results = results;
		this.dataSize = results == null ? 0: results.size();
	}

	public ExternalResult(String returnCode, String message, List<T> results) {
		this(200,returnCode,message,results);
	}

	public ExternalResult(List<T> results) {
		this(ExternalResultCodeEnum.SUCCESS.getCode(), ExternalResultCodeEnum.SUCCESS.getName(), results);
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
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

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

	public Integer getDataSize() {
		return dataSize;
	}

	public void setDataSize(Integer dataSize) {
		this.dataSize = dataSize;
	}
}
