package com.soukuan.util;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import javax.persistence.Id;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
* Title 对象操作工具类, 继承org.apache.commons.lang3.ObjectUtils类
* Author xiebiao@wshifu.com
* DateTime  17-07-19.
* Version V1.0.0
*/
public class ObjectUtils extends org.apache.commons.lang3.ObjectUtils {

    public ObjectUtils() {
        throw new UnsupportedOperationException();
    }

    /**
    * 注解到对象复制，只复制能匹配上的方法。
    * @param annotation
    * @param object
    */
   public static void annotationToObject(Object annotation, Object object){
       if (annotation != null){
           Class<?> annotationClass = annotation.getClass();
           Class<?> objectClass = object.getClass();
           for (Method m : objectClass.getMethods()){
               if (StringUtils.startsWith(m.getName(), "set")){
                   try {
                       String s = StringUtils.uncapitalize(StringUtils.substring(m.getName(), 3));
                       Object obj = annotationClass.getMethod(s).invoke(annotation);
                       if (obj != null && !"".equals(obj.toString())){
                           m.invoke(object, obj);
                       }
                   } catch (Exception e) {
                       // 忽略所有设置失败方法
                   }
               }
           }
       }
   }

   /**
    * 序列化对象
    * @param object
    * @deprecated
    * replaced by <code>serializeByProtostuff(object)</code>
    * @return
    */
   @Deprecated
   public static byte[] serialize(Object object) {
       ObjectOutputStream oos;
       ByteArrayOutputStream baos;
       try {
           if (object != null){
               baos = new ByteArrayOutputStream();
               oos = new ObjectOutputStream(baos);
               oos.writeObject(object);
               return baos.toByteArray();
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
       return null;
   }

   /**
    * 反序列化对象
    * @param bytes
    * @deprecated
    * replaced by <code>deserializerByProtostuff(object,clazz)</code>
    * @return
    */
   @Deprecated
   public static Object unserialize(byte[] bytes) {
       ByteArrayInputStream bais;
       try {
           if (bytes != null && bytes.length > 0){
               bais = new ByteArrayInputStream(bytes);
               ObjectInputStream ois = new ObjectInputStream(bais);
               return ois.readObject();
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
       return null;
   }

   /**
    * 判断对象属性是否为空
    * @param instance
    * @param ignoreId id字段必须以@Id注解标注，否则无效
    * @return
    */
   public static boolean isEmptyObject(Object instance,Boolean ignoreId){
       if(null == instance)
           return true;
       for (Field field : instance.getClass().getDeclaredFields()) {
           field.setAccessible(true);
           try {
               if ("serialVersionUID".equals(field.getName()) || (ignoreId && field.getAnnotation(Id.class) != null))
                   continue;
               if (field.get(instance) != null) { //判断字段是否为空，并且对象属性中的基本都会转为对象类型来判断
                   return false;
               }
           } catch (IllegalAccessException e) {
               e.printStackTrace();
           }
       }
       return true;
   }

   /**
    * 通过protostuff 高效的序列化对象
    * @param obj
    * @param <T>
    * @return
    */
   public static <T> byte[] serializeByProtostuff(T obj) {
       Class<T> cls = (Class<T>) obj.getClass();
       LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
       try {
           Schema<T> schema = RuntimeSchema.getSchema(cls);
           return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
       } catch (Exception e) {
           throw new IllegalStateException(e.getMessage(), e);
       } finally {
           buffer.clear();
       }
   }

   /**
    * 通过protostuff  高效的反序列化对象
    * @param data
    * @param clazz
    * @param <T>
    * @return
    */
   public static <T> T deserializerByProtostuff(byte[] data, Class<T> clazz) {
       try {
           T obj = clazz.newInstance();
           Schema<T> schema = RuntimeSchema.getSchema(clazz);
           ProtostuffIOUtil.mergeFrom(data, obj, schema);
           return obj;
       } catch (Exception e) {
           throw new IllegalStateException(e.getMessage(), e);
       }
   }
}
