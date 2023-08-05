package com.github.desperado2.data.open.api.log.manage.model;


import com.github.desperado2.data.open.api.common.manage.model.PageSearch;
import io.swagger.annotations.ApiModelProperty;

/**
 * 搜索模型
 * @author tu nan
 * @date 2023/2/15
 **/
public class ApiLogSearchModel extends PageSearch {

    @ApiModelProperty("apiId")
    private Long apiId;

    @ApiModelProperty("查询词")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }
}
