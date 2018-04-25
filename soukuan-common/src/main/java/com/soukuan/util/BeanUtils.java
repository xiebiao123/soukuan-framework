package com.soukuan.util;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Title
 * Author xiebiao@wanshifu.com
 * Time 2017/5/15.
 * Version v1.0
 */
@Slf4j
public class BeanUtils extends org.springframework.beans.BeanUtils {

    /**
     *  拷贝List
     * @param clazz
     * @param source
     * @param <T>
     * @return
     */
    public static <T> List<T> copyList(Class<T> clazz, List<?> source) {
        List<T> result = Collections.emptyList();
        if (source != null && source.size() > 0) {
            result = new ArrayList<T>();
            try {
                for (Object object : source) {
                    T target = clazz.newInstance();
                    copyProperties(object, target);
                    result.add(target);
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("can not copyList from " + source + " to " + clazz);
            }
        }
        return result;
    }

    /**
     * 对象拷贝
     *
     * @param source      拷贝源
     * @param targetClazz 目标类型
     * @return 拷贝的对象
     * <p>
     * 不建议对于同类型对象拷贝使用该方法，
     * 反射实现对象拷贝性能不高，应该考虑 实现Cloneable接口并覆盖Object的clone方法实现对象拷贝
     * 或者提供拷贝构造器的方案
     */
    public static <T> T copyObject(Object source, Class<T> targetClazz) {
        try {
            source.getClass().newInstance();
            T target = targetClazz.newInstance();
            copyProperties(source, target);
            return target;
        } catch (Throwable e) {
            throw new IllegalArgumentException("can not copyObject from " + source.getClass() + " to " + targetClazz);
        }
    }

}
