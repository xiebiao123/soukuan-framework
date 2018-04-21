package com.soukuan.web.templ;


import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Title 基于通用MyBatis Mapper插件的Service接口的实现
 * Time 2017/8/17.
 * Version v1.0
 */
public abstract class AbstractService<T> implements Service<T> {

    @Autowired
    protected IBaseCommMapper<T> mapper;

    private Class<T> modelClass;    // 当前泛型真实类型的Class

    public AbstractService() {
        Class<?> clazz = this.getClass();
        Type type;
        while (clazz != null) {
            if(clazz.getName().equals("java.lang.Object")){
                break;
            }
            type = clazz.getGenericSuperclass();
            if(type instanceof ParameterizedType){
                modelClass = (Class<T>) ((ParameterizedType)type).getActualTypeArguments()[0];
                break;
            }
            clazz = clazz.getSuperclass();
        }
    }

    public void save(T model) {
        mapper.insertSelective(model);
    }

    public void save(List<T> models) {
        mapper.insertList(models);
    }

    public int deleteById(Object id) {
        return mapper.deleteByPrimaryKey(id);
    }

    public int deleteByIds(String ids) {
        return mapper.deleteByIds(ids);
    }

    public int update(T model) {
         return mapper.updateByPrimaryKeySelective(model);
    }

    public T findById(Object id) {
        return mapper.selectByPrimaryKey(id);
    }

    public List<T> findByIds(String ids) {
        return mapper.selectByIds(ids);
    }

    public List<T> findByCondition(Condition condition) {
        return mapper.selectByCondition(condition);
    }

    public List<T> findAll() {
        return mapper.selectAll();
    }

    public int countByCondition(Condition condition){return  mapper.selectCountByCondition(condition);}

    @Override
    public T findOne(T t) {
        return mapper.selectOne(t);
    }

    @Override
    public List<T> find(T t) {
        return mapper.select(t);
    }

    @Override
    public T findBy(String fieldName, Object value){
        try {
            T model = modelClass.newInstance();
            Field field = modelClass.getDeclaredField(fieldName);
            if(field.getType().isAssignableFrom(Long.class))
                value = Long.valueOf(String.valueOf(value));
            if(field.getType().isAssignableFrom(Integer.class))
                value = Integer.valueOf(String.valueOf(value));
            field.setAccessible(true);
            field.set(model, value);
            return mapper.selectOne(model);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 检查字段值的唯一性
     * @param fieldName
     * @param value
     * @return
     */
    public Boolean checkExisted(String fieldName, Object value){
        try {
            T model = modelClass.newInstance();
            Field field = modelClass.getDeclaredField(fieldName);
            if(field.getType().isAssignableFrom(Long.class))
                value = Long.valueOf(String.valueOf(value));
            if(field.getType().isAssignableFrom(Integer.class))
                value = Integer.valueOf(String.valueOf(value));
            field.setAccessible(true);
            field.set(model, value);
            Integer result = mapper.selectCount(model);
            return result > 0;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
