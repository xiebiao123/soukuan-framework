package com.soukuan.configuration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soukuan.component.RedisHelper;
import com.soukuan.properties.RedisProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * Title
 * Time 2017/5/31.
 * Version v1.0
 */
@Slf4j
@Configuration
@ConditionalOnClass(Jedis.class)
@EnableConfigurationProperties(RedisProperties.class)
@ConditionalOnProperty(prefix = RedisProperties.PREFIX, name = "enable", matchIfMissing = true)
@EnableCaching //加上这个注解是的支持缓存注解
public class RedisConfiguration extends CachingConfigurerSupport {

    @Resource
    private RedisProperties redisProperties;

    /**
     * 自定义生成redis-key
     *
     * @return
     */
    @Override
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object o, Method method, Object... objects) {
                StringBuilder sb = new StringBuilder();
                sb.append(o.getClass().getName()).append(".");
                sb.append(method.getName()).append(".");
                for (Object obj : objects) {
                    sb.append(obj.toString());
                }
                log.info("keyGenerator=" + sb.toString());
                return sb.toString();
            }
        };
    }

    /**
     * 连接redis的工厂类
     * @return
     */
    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName(redisProperties.getRedisHost());
        factory.setPort(redisProperties.getRedisPort());
        factory.setTimeout(redisProperties.getRedisPoolMaxWait());
        factory.setPassword(redisProperties.getRedisPassword());
        factory.setDatabase(redisProperties.getDatabase());
        return factory;
    }

    /**
     * 设置RedisCacheManager
     * 使用cache注解管理redis缓存
     * @return
     */
    @Bean
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate());
        return redisCacheManager;
    }

    /**
     * 配置RedisTemplate
     * 设置添加序列化器
     * key 使用string序列化器
     * value 使用Json序列化器
     * 还有一种简答的设置方式，改变defaultSerializer对象的实现。
     * @return
     */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate() {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(mapper);
        template.setValueSerializer(serializer);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisHelper redisHelper() {
        this.checkConfigFull();
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(redisProperties.getRedisPoolMaxIdle());
        config.setMaxWaitMillis(redisProperties.getRedisPoolMaxWait());
        config.setTestOnBorrow(redisProperties.isRedisPoolTestOnBorrow());
        config.setTestOnReturn(redisProperties.isRedisPoolTestOnReturn());
        JedisPool connectionPool = new JedisPool(config, redisProperties.getRedisHost(), redisProperties.getRedisPort(), 2000, redisProperties.getRedisPassword());
        RedisHelper redisHelper = new RedisHelper(connectionPool);
        log.info("【soukuan spring boot component】 'RedisHelper' init successful!");
        return redisHelper;
    }

    private void checkConfigFull() {
        if (StringUtils.isEmpty(redisProperties.getRedisHost()) || redisProperties.getRedisPort() == null) {
            throw new RuntimeException(" properties: redisHost , redisPort is necessary when using soukuan-redis! please check and add these properties in your application" +
                    ".properties");
        }
    }

}
