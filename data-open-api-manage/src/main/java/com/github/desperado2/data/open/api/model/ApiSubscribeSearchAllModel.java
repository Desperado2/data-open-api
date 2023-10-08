package com.github.desperado2.data.open.api.model;


import com.github.desperado2.data.open.api.common.manage.model.PageSearch;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 接口订阅审核模型
 * @author tu nan
 * @date 2023/2/15
 **/
@ApiModel(value = "ApiSubscribeSearchAllModel", description = "订阅数据搜索模型")
public class ApiSubscribeSearchAllModel extends PageSearch {

    @ApiModelProperty("通过状态，0-待审批，1-通过，2-不通过")
    private Integer status;

    @ApiModelProperty("订阅人名称")
    private Integer username;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getUsername() {
        return username;
    }

    public void setUsername(Integer username) {
        this.username = username;
    }
}
