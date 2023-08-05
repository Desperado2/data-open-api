package com.github.desperado2.data.open.api.common.manage.model;

import io.swagger.annotations.ApiModelProperty;

/**
 * 分页结果
 *
 * @author tu nan
 * @date 2023/2/7
 **/
public class PageResults<T> {
    @ApiModelProperty("分页结果数据")
    private T list;

    @ApiModelProperty("分页信息")
    private PageInfo pagination;

    public PageResults() {
    }

    public PageResults(T list, PageInfo pagination) {
        this.list = list;
        this.pagination = pagination;
    }

    public T getList() {
        return list;
    }

    public void setList(T list) {
        this.list = list;
    }

    public PageInfo getPagination() {
        return pagination;
    }

    public void setPagination(PageInfo pagination) {
        this.pagination = pagination;
    }
}
