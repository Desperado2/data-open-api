package com.github.desperado2.data.open.api.user.manage.model;

import com.github.desperado2.data.open.api.common.manage.model.PageSearch;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户搜索模型
 * @author tu nan
 * @date 2023/2/9
 **/
public class UserSearchModel extends PageSearch {

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("状态")
    private Integer status;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
