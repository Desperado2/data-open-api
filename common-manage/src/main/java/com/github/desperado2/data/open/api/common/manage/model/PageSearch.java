package com.github.desperado2.data.open.api.common.manage.model;

import io.swagger.annotations.ApiModelProperty;

/**
 * 搜索模型
 *
 * @author tu nan
 * @date 2023/2/7
 **/
public class PageSearch {

    @ApiModelProperty(
            name = "startNumber",
            value = "分页开始数值",
            example = "1",
            hidden = true
    )
    private Integer startNumber;

    @ApiModelProperty(
            name = "pageSize",
            value = "每页多少数据",
            example = "10"
    )
    private Integer pageSize;

    @ApiModelProperty(
            name = "current",
            value = "当前第几页",
            example = "1"
    )
    private Integer current;

    public Integer getStartNumber() {
        return startNumber;
    }

    public void setStartNumber(Integer startNumber) {
        this.startNumber = startNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }
}
