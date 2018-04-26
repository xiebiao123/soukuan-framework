package com.soukuan.web.page;

import io.swagger.annotations.ApiParam;

/**
 * Title 处理分页时接受的参数为空
 * DateTime 17-3-7 上午9:45
 * Version V1.0.0
 */
public class Pager {

    /**
     * 当前页
     */
    @ApiParam(value = "当前页数，默认1", defaultValue = "1")
    private Integer pageNum = 1;

    /**
     * 每页显示记录数
     */
    @ApiParam(value = "每页显示记录数,默认10，-1代表不分页", defaultValue = "10")
    private Integer pageSize = 10;

    public Pager(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public Pager() {
    }


    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        if (pageNum != null)
            this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        if (pageSize != null)
            this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "pageNum:" + getPageNum() + ";" + "pageSize:" + getPageSize();
    }
}
