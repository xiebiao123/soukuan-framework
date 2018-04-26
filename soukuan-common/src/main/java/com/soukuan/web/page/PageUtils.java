package com.soukuan.web.page;

import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Title 分页工具类
 * Author shenxin@wshifu.com
 * DateTime  17-8-1.
 * Version V1.0.0
 */
public class PageUtils {

    /**
     * 获取简单分页对象
     * @param list 数据集合
     * @param <T> 
     * @return
     */
    public static  <T> SimplePageInfo<T> getSimplePageInfo(List<T> list) {
        PageInfo<T> pageInfo = new PageInfo<>(list);
        SimplePageInfo<T> simplePageInfo = new SimplePageInfo<>(list);
        simplePageInfo.setList(list);
        simplePageInfo.setPageNum(pageInfo.getPageNum());
        simplePageInfo.setPages(pageInfo.getPages());
        simplePageInfo.setPageSize(pageInfo.getPageSize());
        simplePageInfo.setTotal(pageInfo.getTotal());
        return simplePageInfo;
    }
}
