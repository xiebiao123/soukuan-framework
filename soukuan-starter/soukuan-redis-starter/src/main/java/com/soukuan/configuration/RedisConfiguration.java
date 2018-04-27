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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Title
 * Author jirenhe@soukuan.com
 * Time 2017/5/31.
 * Version v1.0
 */
@Configuration
@ConditionalOnClass(Jedis.class)
@EnableConfigurationProperties(RedisProperties.class)
@ConditionalOnProperty(prefix = RedisProperties.PREFIX, name = "enable", matchIfMissing = true)
@Slf4j
public class RedisConfiguration {

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

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
        if (StringUtils.isEmpty(redisProperties.getRedisHost()) || StringUtils.isEmpty(redisProperties.getRedisPassword()) || redisProperties.getRedisPort() == null) {
            throw new RuntimeException(" properties: redisHost, redisPassword, redisPort is necessary when using soukuan-redis! please check and add these properties in your application" +
                    ".properties");
        }
    }

}
