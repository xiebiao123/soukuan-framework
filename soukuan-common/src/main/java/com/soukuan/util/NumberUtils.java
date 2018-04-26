package com.soukuan.util;

import java.math.BigDecimal;

/**
 * Title
 * Author jirenhe@wanshifu.com
 * Time 2018/3/20.
 * Version v1.0
 */
public class NumberUtils extends org.apache.commons.lang3.math.NumberUtils{

    public NumberUtils(){
        throw new UnsupportedOperationException();
    }

    /**
     * 判断bigDecimal是否是正数（如果为参数null，返回false）
     * @param bigDecimal 判断的对象
     * @return 是否是正数
     */
    public static boolean isPositive(BigDecimal bigDecimal){
        return bigDecimal != null && bigDecimal.compareTo(BigDecimal.ZERO) > 0;
    }
}