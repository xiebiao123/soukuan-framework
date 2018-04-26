package com.soukuan.util;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.cglib.beans.BeanMap;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * Title
 * Author jirenhe@wanshifu.com
 * Time 2017/11/15.
 * Version v1.0
 */
public class MapUtils extends org.apache.commons.collections.MapUtils {

    public MapUtils() {
        throw new UnsupportedOperationException();
    }


    /**
     * 将对象转成map
     *
     * @param obj             待转换实例对象
     * @param removeNullValue 是否过滤掉空值
     * @return
     */
    public static Map<String, Object> objectToMap(Object obj, Boolean removeNullValue) {
        if (Objects.isNull(obj))
            return EMPTY_MAP;
        Map<String, Object> map = new HashedMap(BeanMap.create(obj));
        if (removeNullValue)
            removeNullValue(map);
        return map;
    }


    /**
     * 移除map中的value空值
     *
     * @param map
     * @return
     */
    public static void removeNullValue(Map map) {
        Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            if (Objects.isNull(entry.getValue()))
                iterator.remove();
        }
    }

}
