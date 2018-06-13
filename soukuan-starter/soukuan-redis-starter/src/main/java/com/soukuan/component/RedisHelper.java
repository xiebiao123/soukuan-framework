package com.soukuan.component;

import com.soukuan.util.ObjectUtils;
import com.soukuan.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Jedis Cache 工具类
 *
 * @author xiebiao
 * @version 2015-9-21
 */
@Slf4j
public class RedisHelper {

    private JedisPool connectionPool;

    public RedisHelper(JedisPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    /**
     * 产生key值，方便在redis查询时辩认
     * @param appName       app名称简写
     * @param moduleName    app模块名称简写
     * @param apiName       app模块下的接口名称简写
     * @param key           键值
     * @return
     */
    public String genKey(String appName,String moduleName,String apiName,String key){
        return String.format("[%1$s:%2$s:%3$s:%4$s]",appName,moduleName,apiName,key);
    }

    /**
     * 产生key值，方便在redis查询时辩认
     * @param appName       app名称简写
     * @param moduleName    app模块名称简写
     * @param apiName       app模块下的接口名称简写
     * @param key           键值
     * @return
     */
    public String genKeyByDate(String appName,String moduleName,String apiName,String dateFormat,String key){
        String dateVal = new SimpleDateFormat(dateFormat).format(new Date());
        return String.format("[%1$s:%2$s:%3$s:%4$s:%5$s]",appName,moduleName,apiName,dateVal,key);
    }

    /**
     * 获取缓存
     *
     * @param key 键
     * @return 值
     */
    public String get(String key) {
        String value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                value = jedis.get(key);
                value = StringUtils.isNotBlank(value) && !"nil".equalsIgnoreCase(value) ? value : null;
            }
            log.debug("get {} = {}", key, value);
        } catch (Exception e) {
            log.warn("get {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    /**
     * 获取缓存
     *
     * @param key 键
     * @return 值
     */
    public <T> T getObject(String key,Class<T>  clazz) {
        T value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                value = toObject(jedis.get(getBytesKey(key)),clazz);
            }
            log.debug("getObject {} = {}", key, value);
        } catch (Exception e) {
            log.warn("getObject {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    /**
     * 设置缓存
     *
     * @param key          键
     * @param value        值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public String set(String key, String value, int cacheSeconds) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.set(key, value);
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            log.debug("set {} = {}", key, value);
        } catch (Exception e) {
            log.warn("set {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 指定key值自增1
     * @param key
     * @return
     */
    public Long incr(String key) {
        Long result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.incr(key);
            log.debug("incr {} to {}", key, result);
        } catch (Exception e) {
            log.warn("incr {} to {}", key, result, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 指定key值自增1
     * @param key
     * @return
     */
    public Long incr(String key,int cacheSeconds) {
        Long result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.incr(key);
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            log.debug("incr {} to {}", key, result);
        } catch (Exception e) {
            log.warn("incr {} to {}", key, result, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 指定key值自减1
     * @param key
     * @return
     */
    public Long decr(String key) {
        Long result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.decr(key);
            log.debug("decr {} to {}", key, result);
        } catch (Exception e) {
            log.warn("decr {} to {}", key, result, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }


    /**
     * 指定key值自减1
     * @param key
     * @return
     */
    public Long decr(String key,int cacheSeconds) {
        Long result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.decr(key);
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            log.debug("desc {} to {}", key, result);
        } catch (Exception e) {
            log.warn("desc {} to {}", key, result, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 设置缓存当天24:00过期
     *
     * @param key          键
     * @param value        值
     * @return
     */
    public String setObjectTodayOvertime(String key, Object value) {
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        double diff = (cal.getTimeInMillis() - System.currentTimeMillis())/1000;
        int cacheSeconds = (int)diff;
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.set(getBytesKey(key), toBytes(value));
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            log.debug("setObject {} = {}", key, value);
        } catch (Exception e) {
            log.warn("setObject {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 设置缓存
     *
     * @param key          键
     * @param value        值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public String setObject(String key, Object value, int cacheSeconds) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.set(getBytesKey(key), toBytes(value));
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            log.debug("setObject {} = {}", key, value);
        } catch (Exception e) {
            log.warn("setObject {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 获取List缓存
     *
     * @param key 键
     * @return 值
     */
    public List<String> getList(String key) {
        List<String> value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                value = jedis.lrange(key, 0, -1);

            }
            log.debug("getList {} = {}", key, value);
        } catch (Exception e) {
            log.warn("getList {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    /**
     * 获取List缓存
     *
     * @param key 键
     * @return 值
     */
    public <T> List<T> getObjectList(String key,Class<T> clazz) {
       List<T> value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                List<byte[]> list = jedis.lrange(getBytesKey(key), 0, -1);
                value = new ArrayList<T>();
                for (byte[] bs : list) {
                    value.add(toObject(bs,clazz));
                }
            }
            log.debug("getObjectList {} = {}", key, value);
        } catch (Exception e) {
            log.warn("getObjectList {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    /**
     * 设置List缓存
     *
     * @param key          键
     * @param value        值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public long setList(String key, List<String> value, int cacheSeconds) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                jedis.del(key);
            }
            result = jedis.rpush(key, value.toArray(new String[value.size()]));
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            log.debug("setList {} = {}", key, value);
        } catch (Exception e) {
            log.warn("setList {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 设置List缓存
     *
     * @param key          键
     * @param value        值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public <T> long setObjectList(String key, List<T> value, int cacheSeconds) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                jedis.del(key);
            }
            byte[][] bytesObjectArray = new byte[value.size()][];
            for (int i = 0; i <value.size() ; i++) {
                bytesObjectArray[i] = toBytes(value.get(i));
            }
            result = jedis.rpush(getBytesKey(key), bytesObjectArray);
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            log.debug("setObjectList {} = {}", key, value);
        } catch (Exception e) {
            log.warn("setObjectList {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 向List缓存中添加值
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public long listAdd(String key, String... value) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.rpush(key, value);
            log.debug("listAdd {} = {}", key, value);
        } catch (Exception e) {
            log.warn("listAdd {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 向List缓存中添加值
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public long listObjectAdd(String key, Object... value) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            List<byte[]> list = new ArrayList<>();
            for (Object o : value) {
                list.add(toBytes(o));
            }
            result = jedis.rpush(getBytesKey(key), (byte[][]) list.toArray());
            log.debug("listObjectAdd {} = {}", key, value);
        } catch (Exception e) {
            log.warn("listObjectAdd {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 获取缓存
     *
     * @param key 键
     * @return 值
     */
    public Set<String> getSet(String key) {
        Set<String> value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                value = jedis.smembers(key);

            }
            log.debug("getSet {} = {}", key, value);
        } catch (Exception e) {
            log.warn("getSet {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    /**
     * 获取缓存
     *
     * @param key 键
     * @return 值
     */
    public <T> Set<T> getObjectSet(String key,Class<T> clazz) {
        Set<T> value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                value = new HashSet<>();
                Set<byte[]> set = jedis.smembers(getBytesKey(key));
                for (byte[] bs : set) {
                    value.add(toObject(bs,clazz));
                }

            }
            log.debug("getObjectSet {} = {}", key, value);
        } catch (Exception e) {
            log.warn("getObjectSet {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    /**
     * 设置Set缓存
     *
     * @param key          键
     * @param value        值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public long setSet(String key, Set<String> value, int cacheSeconds) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                jedis.del(key);
            }
            result = jedis.sadd(key, (String[]) value.toArray());
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            log.debug("setSet {} = {}", key, value);
        } catch (Exception e) {
            log.warn("setSet {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 设置Set缓存
     *
     * @param key          键
     * @param value        值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public long setObjectSet(String key, Set<Object> value, int cacheSeconds) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                jedis.del(key);
            }
            Set<byte[]> set = new HashSet<>();
            for (Object o : value) {
                set.add(toBytes(o));
            }
            result = jedis.sadd(getBytesKey(key), (byte[][]) set.toArray());
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            log.debug("setObjectSet {} = {}", key, value);
        } catch (Exception e) {
            log.warn("setObjectSet {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 向Set缓存中添加值
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public long setSetAdd(String key, String... value) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.sadd(key, value);
            log.debug("setSetAdd {} = {}", key, value);
        } catch (Exception e) {
            log.warn("setSetAdd {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 向Set缓存中添加值
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public long setSetObjectAdd(String key, Object... value) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            Set<byte[]> set = new HashSet<>();
            for (Object o : value) {
                set.add(toBytes(o));
            }
            result = jedis.rpush(getBytesKey(key), (byte[][]) set.toArray());
            log.debug("setSetObjectAdd {} = {}", key, value);
        } catch (Exception e) {
            log.warn("setSetObjectAdd {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 获取Map缓存
     *
     * @param key 键
     * @return 值
     */
    public Map<String, String> getMap(String key) {
        Map<String, String> value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                value = jedis.hgetAll(key);

            }
            log.debug("getMap {} = {}", key, value);
        } catch (Exception e) {
            log.warn("getMap {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    /**
     * 获取Map缓存
     *
     * @param key 键
     * @return 值
     */
    public <T> Map<String, T> getObjectMap(String key,Class<T> clazz) {
        Map<String, T> value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                value = new HashMap<>();
                Map<byte[], byte[]> map = jedis.hgetAll(getBytesKey(key));
                for (Map.Entry<byte[], byte[]> e : map.entrySet()) {
                    value.put(StringUtils.toString(e.getKey()), toObject(e.getValue(),clazz));
                }

            }
            log.debug("getObjectMap {} = {}", key, value);
        } catch (Exception e) {
            log.warn("getObjectMap {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    /**
     * 设置Map缓存
     *
     * @param key          键
     * @param value        值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public String setMap(String key, Map<String, String> value, int cacheSeconds) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                jedis.del(key);
            }
            result = jedis.hmset(key, value);
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            log.debug("setMap {} = {}", key, value);
        } catch (Exception e) {
            log.warn("setMap {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 设置Map缓存
     *
     * @param key          键
     * @param value        值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public String setObjectMap(String key, Map<String, Object> value, int cacheSeconds) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                jedis.del(key);
            }
            Map<byte[], byte[]> map = new HashMap<>();
            for (Map.Entry<String, Object> e : value.entrySet()) {
                map.put(getBytesKey(e.getKey()), toBytes(e.getValue()));
            }
            result = jedis.hmset(getBytesKey(key), map);
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            log.debug("setObjectMap {} = {}", key, value);
        } catch (Exception e) {
            log.warn("setObjectMap {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 向Map缓存中添加值
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public String mapPut(String key, Map<String, String> value) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.hmset(key, value);
            log.debug("mapPut {} = {}", key, value);
        } catch (Exception e) {
            log.warn("mapPut {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 向Map缓存中添加值
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public String mapObjectPut(String key, Map<String, Object> value) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            Map<byte[], byte[]> map = new HashMap<>();
            for (Map.Entry<String, Object> e : value.entrySet()) {
                map.put(getBytesKey(e.getKey()), toBytes(e.getValue()));
            }
            result = jedis.hmset(getBytesKey(key), map);
            log.debug("mapObjectPut {} = {}", key, value);
        } catch (Exception e) {
            log.warn("mapObjectPut {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 移除Map缓存中的值
     *
     * @param key    键
     * @param mapKey 值
     * @return
     */
    public long mapRemove(String key, String mapKey) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.hdel(key, mapKey);
            log.debug("mapRemove {}  {}", key, mapKey);
        } catch (Exception e) {
            log.warn("mapRemove {}  {}", key, mapKey, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 移除Map缓存中的值
     *
     * @param key    键
     * @param mapKey 值
     * @return
     */
    public long mapObjectRemove(String key, String mapKey) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.hdel(getBytesKey(key), getBytesKey(mapKey));
            log.debug("mapObjectRemove {}  {}", key, mapKey);
        } catch (Exception e) {
            log.warn("mapObjectRemove {}  {}", key, mapKey, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 判断Map缓存中的Key是否存在
     *
     * @param key    键
     * @param mapKey 值
     * @return
     */
    public boolean mapExists(String key, String mapKey) {
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.hexists(key, mapKey);
            log.debug("mapExists {}  {}", key, mapKey);
        } catch (Exception e) {
            log.warn("mapExists {}  {}", key, mapKey, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 判断Map缓存中的Key是否存在
     *
     * @param key    键
     * @param mapKey 值
     * @return
     */
    public boolean mapObjectExists(String key, String mapKey) {
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.hexists(getBytesKey(key), getBytesKey(mapKey));
            log.debug("mapObjectExists {}  {}", key, mapKey);
        } catch (Exception e) {
            log.warn("mapObjectExists {}  {}", key, mapKey, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 删除缓存
     *
     * @param key 键
     * @return
     */
    public long del(String key) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                result = jedis.del(key);
                log.debug("del {}", key);
            } else {
                log.debug("del {} not exists", key);
            }
        } catch (Exception e) {
            log.warn("del {}", key, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 删除缓存
     *
     * @param key 键
     * @return
     */
    public long delObject(String key) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                result = jedis.del(getBytesKey(key));
                log.debug("delObject {}", key);
            } else {
                log.debug("delObject {} not exists", key);
            }
        } catch (Exception e) {
            log.warn("delObject {}", key, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 缓存是否存在
     *
     * @param key 键
     * @return
     */
    public boolean exists(String key) {
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.exists(key);
            log.debug("exists {}", key);
        } catch (Exception e) {
            log.warn("exists {}", key, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 缓存是否存在
     *
     * @param key 键
     * @return
     */
    public boolean existsObject(String key) {
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.exists(getBytesKey(key));
            log.debug("existsObject {}", key);
        } catch (Exception e) {
            log.warn("existsObject {}", key, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 获取资源
     *
     * @return
     * @throws redis.clients.jedis.exceptions.JedisException
     */
    public Jedis getResource() throws JedisException {
        Jedis jedis;
        try {
            jedis = connectionPool.getResource();
        } catch (JedisException e) {
            log.warn("getResource.", e);
            throw e;
        }
        return jedis;
    }

    /**
     * 归还资源
     *
     * @param jedis
     */
    public void returnBrokenResource(Jedis jedis) {
        if (jedis != null) {
            connectionPool.returnBrokenResource(jedis);
        }
    }

    /**
     * 释放资源
     *
     * @param jedis
     */
    public void returnResource(Jedis jedis) {
        if (jedis != null) {
            connectionPool.returnResource(jedis);
        }
    }

    /**
     * 获取byte[]类型Key
     *
     * @param object
     * @return
     */
    public byte[] getBytesKey(Object object) {
        if (object instanceof String) {
            return StringUtils.getBytes((String) object);
        } else {
            return ObjectUtils.serializeByProtostuff(object);
        }
    }

    /**
     * Object转换byte[]类型
     *
     * @param object
     * @return
     */
    public byte[] toBytes(Object object) {
        return ObjectUtils.serializeByProtostuff(object);
    }


    /**
     * byte[]型转换Object
     * @param bytes
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T toObject(byte[] bytes,Class<T>  clazz) {
        return ObjectUtils.deserializerByProtostuff(bytes,clazz);
    }
}
