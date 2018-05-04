package com.soukuan.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Title
 * Time 2017/5/31.
 * Version v1.0
 */
@Data
@ConfigurationProperties(prefix = RedisProperties.PREFIX)
public class RedisProperties {

    public static final String PREFIX = "soukuan.redis";

    private boolean enable = true;
    /**
     * redis config maxActive
     */
    private int redisPoolMaxActive=1024;

    /**
     * redis config maxIdle
     */
    private int redisPoolMaxIdle=200;

    /**
     * redis config maxWait
     */
    private int redisPoolMaxWait=1000;

    /**
     * redis config testOnBorrow
     */
    private boolean redisPoolTestOnBorrow=false;

    /**
     * redis config testOnReturn
     */
    private boolean redisPoolTestOnReturn=false;

    private int database = 0;

    /**
     * redis host
     */
    private String redisHost;

    /**
     * redis port
     */
    private Integer redisPort;

    /**
     * redis password
     */
    private String redisPassword;


    private int defaultCacheTime = 1024;

    /**
     * 缓存时间：30天
     */
    private int cacheMon = 3600 * 24 * 30;

    /**
     * 缓存时间：1天
     */
    private int cacheDay = 3600 * 24;

    /**
     * 缓存时间：1小时
     */
    private int cacheHour = 3600;

    /**
     * 缓存时间：1分钟
     */
    private int cacheMin = 60;

}
