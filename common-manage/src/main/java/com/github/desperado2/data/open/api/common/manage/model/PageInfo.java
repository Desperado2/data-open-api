package com.github.desperado2.data.open.api.common.manage.model;

import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformRuntimeException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 分页信息
 *
 * @author tu nan
 * @date 2023/2/7
 **/
@ApiModel(description = "分页结果实体")
public class PageInfo implements Serializable {

    private static final long serialVersionUID = 1414975513183194980L;
    @ApiModelProperty("当前页开始数字")
    private Long startNumber;

    @ApiModelProperty("当前页")
    private Long current;

    @ApiModelProperty("总页数")
    private Long totalPage;

    @ApiModelProperty("每页记录数")
    private Long pageSize;

    @ApiModelProperty("总记录数")
    private Long total;

    public Long getStartNumber() {
        return startNumber;
    }

    public void setStartNumber(Long startNumber) {
        this.startNumber = startNumber;
    }

    public Long getCurrent() {
        return current;
    }

    public void setCurrent(Long current) {
        this.current = current;
    }

    public Long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public PageInfo() {
    }

    public PageInfo(long current, long pageSize, long total) {
        if (pageSize >= 1 && current >= 1) {
            this.pageSize = pageSize;
            this.current = current;
            this.total = (long) total;
            this.startNumber = (current - 1) * pageSize;
            if (total == 0) {
                this.totalPage = 1L;
            } else {
                long temCount = total % pageSize;
                long totalPage = total / pageSize;
                if (temCount != 0) {
                    ++totalPage;
                }

                this.totalPage = (long) totalPage;
            }

        } else {
            throw new DataOpenPlatformRuntimeException("每页记录数或者当前页的值小于1");
        }
    }
}
