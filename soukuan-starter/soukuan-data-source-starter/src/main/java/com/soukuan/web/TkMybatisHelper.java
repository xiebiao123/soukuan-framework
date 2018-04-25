package com.soukuan.web;

import com.soukuan.util.ArrayUtils;
import com.soukuan.util.BeanUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.mapperhelper.EntityHelper;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Title
 * Author jirenhe@wanshifu.com
 * Time 2017/6/27.
 * Version v1.0
 */
public class TkMybatisHelper {

    private TkMybatisHelper() {
    }

    /**
     * 按对象生成Example对象
     * @param object 实体对象
     * @param clazz 实体类
     * @param ignoreProperty 忽略的属性
     * @return Example对象
     * @throws Exception
     */
    public static Example getExample(Object object, Class<?> clazz, String... ignoreProperty) throws Exception {
        Example example = new Example(clazz);
        Example.Criteria criteria = example.createCriteria();
        constructCriteria(object, clazz, criteria, ignoreProperty);
        return example;
    }

    /**
     * 按对象生成Condition对象
     * @param object 实体对象
     * @param clazz 实体类
     * @param ignoreProperty 忽略的属性
     * @return Condition对象
     * @throws Exception
     */
    public static Condition getCondition(Object object, Class<?> clazz, String... ignoreProperty) throws Exception {
        Condition condition = new Condition(clazz);
        Condition.Criteria criteria = condition.createCriteria();
        constructCriteria(object, clazz, criteria, ignoreProperty);
        return condition;
    }

    /**
     * 构建Criteria
     * @param object 实体对象
     * @param clazz 实体类
     * @param criteria criteria实例
     * @param ignoreProperty 忽略的属性
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static Example.Criteria constructCriteria(Object object, Class<?> clazz, Example.Criteria criteria, String[] ignoreProperty) throws IllegalAccessException, InvocationTargetException {
        EntityTable entityTable = EntityHelper.getEntityTable(clazz);
        Map<String, EntityColumn> propertyMap = entityTable.getPropertyMap();
        String propertyName;
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(object.getClass());
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            propertyName = propertyDescriptor.getName();
            if ("class".equals(propertyName)) continue;
            if (ArrayUtils.contains(ignoreProperty, propertyName)) continue;
            if (propertyMap.get(propertyName) != null) {
                Object propertyValue = propertyDescriptor.getReadMethod().invoke(object);
                if (null != propertyValue && !"".equals(propertyValue)) {
                    if (propertyDescriptor.getPropertyType() == String.class) {
                        String strValue = (String) propertyValue;
                        if (strValue.contains("%") || strValue.contains("*")) {
                            criteria.andLike(propertyName, strValue);
                        } else {
                            criteria.andEqualTo(propertyName, strValue);
                        }
                    } else {
                        criteria.andEqualTo(propertyName, propertyValue);
                    }
                }
            }
        }
        return criteria;
    }

}
