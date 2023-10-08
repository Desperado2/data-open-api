package com.github.desperado2.data.open.api.model;


import com.github.desperado2.data.open.api.common.manage.model.PageSearch;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * 接口订阅审核模型
 * @author tu nan
 * @date 2023/2/15
 **/
@ApiModel(value = "ApiSubscribeSearchModel", description = "订阅数据用户搜索模型")
public class ApiSubscribeSearchModel extends PageSearch {

    @ApiModelProperty("通过状态，0-待审批，1-通过，2-不通过")
    @NotBlank(message = "status不能为空")
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
