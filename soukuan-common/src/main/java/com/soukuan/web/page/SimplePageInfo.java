package com.soukuan.web.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Title
 * Author jirenhe@wanshifu.com
 * Time 2017/5/19.
 * Version v1.0
 */
@ApiModel
@Data
public class SimplePageInfo<T> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "当前页", example = "1")
    private int pageNum;

    @ApiModelProperty(value = "每页条数", example = "10")
    private int pageSize;

    @ApiModelProperty(value = "总数", example = "100")
    private long total;

    @ApiModelProperty(value = "总页数", example = "10")
    private int pages;

    @ApiModelProperty(value = "结果集")
    private List<T> list;

    public SimplePageInfo() {
    }

    public SimplePageInfo(List<T> list) {
    }

}
