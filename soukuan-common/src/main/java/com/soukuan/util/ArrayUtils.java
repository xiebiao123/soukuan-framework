package com.soukuan.util;

import com.soukuan.util.function.CheckedConsumer;

/**
 * Title
 * Author jirenhe@wanshifu.com
 * Time 2017/6/30.
 * Version v1.0
 */
public class ArrayUtils extends org.apache.commons.lang.ArrayUtils {

    public static <T> void foreach(T[] objects, CheckedConsumer<T> action) {
        try {
            if (objects != null && objects.length > 0) {
                for (T object : objects) {
                    action.accept(object);
                }
            }
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public static void main(String[] args) {
        String[] strings = new String[]{"asd", "2eq2", "dasd", "d12313"};
        foreach(strings, System.out::println);

        Integer[] ints = new Integer[]{1, 2, 3, 4, 0};
        foreach(ints, item -> System.out.println(4 / item));
    }
}
