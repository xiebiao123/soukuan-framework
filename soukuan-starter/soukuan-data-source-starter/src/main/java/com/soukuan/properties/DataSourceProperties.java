package com.soukuan.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Title
 * Author xiebiao@soukuan.com
 * Time 2017/5/31.
 * Version v1.0
 */
@ConfigurationProperties(prefix = DataSourceProperties.PREFIX)
@Data
public class DataSourceProperties {

    public static final String PREFIX = "soukuan.dataSource";

    private String driverClassName = "com.mysql.jdbc.Driver";

    private boolean enable;

    private String url;

    private String username;

    private String password;

    private int initialSize = 5;

    private int minIdle = 5;

    private int maxActive = 20;

    private int maxWait = 60000;

    private int timeBetweenEvictionRunsMillis = 60000;

    private int minEvictableIdleTimeMillis = 300000;

    private String validationQuery = "SELECT 1 FROM DUAL";

    private boolean testWhileIdle = true;

    private boolean testOnBorrow = false;

    private boolean testOnReturn = false;

    private boolean poolPreparedStatements = false;

    private int maxPoolPreparedStatementPerConnectionSize = 20;

    private String filters = "stat,wall,log4j";

    private String connectionProperties = "servlets.stat.mergeSql=true;servlets.stat.slowSqlMillis=5000";

    private Servlet servlet = new Servlet();

    private Filter filter = new Filter();


}
