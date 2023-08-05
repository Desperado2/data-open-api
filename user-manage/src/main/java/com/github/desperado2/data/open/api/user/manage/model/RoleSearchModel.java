package com.github.desperado2.data.open.api.user.manage.model;

import com.github.desperado2.data.open.api.common.manage.model.PageSearch;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 角色
 * @author tu nan
 * @date 2023/2/10
 **/
@ApiModel(value = "RoleSearchModel", description = "角色搜索请求模型")
public class RoleSearchModel extends PageSearch {

    @ApiModelProperty("角色名称")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
