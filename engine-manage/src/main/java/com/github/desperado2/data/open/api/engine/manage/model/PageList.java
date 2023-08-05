package com.github.desperado2.data.open.api.engine.manage.model;


import java.util.List;
import java.util.Map;

/**
 * 分页查询
 * @author tu nan
 * @date 2023/5/25
 **/
public class PageList {


    /**
     * 当前页
     */

    private int page;

    /**
     * 每页显示的记录条数
     */
    private int pageSize;

    /**
     * 当前偏移量
     */
    private int offset;

    /**
     * 数据集合（需要显示在网页中的数据）
     */
    private List<Map> dataList;

    /**
     * 总记录条数
     */
    private int totalSize;

    /**
     * 总页数
     */
    private int totalPage;


    public PageList(Integer page) {
        this.page = page;
        this.pageSize = 5;
        this.setOffset((page - 1) * 5);
    }

    public PageList(Integer page, Integer pageSize) {
        this.page = page;
        this.pageSize = pageSize;
        this.setOffset((page - 1) * pageSize);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public List<Map> getDataList() {
        return dataList;
    }

    public void setDataList(List<Map> dataList) {
        this.dataList = dataList;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
        this.setTotalPage(totalSize % pageSize == 0 ? totalSize / pageSize : totalSize / pageSize + 1);
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
