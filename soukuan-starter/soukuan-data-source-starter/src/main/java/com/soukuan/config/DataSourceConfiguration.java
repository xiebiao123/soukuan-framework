package com.soukuan.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.soukuan.properties.DataSourceProperties;
import com.soukuan.web.filter.DruidStatFilter;
import com.soukuan.web.servlet.DruidStatViewServlet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collections;

/**
 * Title
 * Author xiebiao@soukuan.com
 * Time 2017/5/31.
 * Version v1.0
 */

@Configuration
@EnableConfigurationProperties(DataSourceProperties.class)
@ConditionalOnProperty(prefix = DataSourceProperties.PREFIX, name = "enable", matchIfMissing = true)
@Slf4j
public class DataSourceConfiguration {

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Bean
    @Primary
    public DataSource dataSource() throws SQLException {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(dataSourceProperties.getUrl());
        datasource.setUsername(dataSourceProperties.getUsername());
        datasource.setPassword(dataSourceProperties.getPassword());
        datasource.setDriverClassName(dataSourceProperties.getDriverClassName());
        //configuration
        datasource.setInitialSize(dataSourceProperties.getInitialSize());
        datasource.setMinIdle(dataSourceProperties.getMinIdle());
        datasource.setMaxActive(dataSourceProperties.getMaxActive());
        datasource.setMaxWait(dataSourceProperties.getMaxWait());
        datasource.setTimeBetweenEvictionRunsMillis(dataSourceProperties.getTimeBetweenEvictionRunsMillis());
        datasource.setMinEvictableIdleTimeMillis(dataSourceProperties.getMinEvictableIdleTimeMillis());
        datasource.setValidationQuery(dataSourceProperties.getValidationQuery());
        datasource.setTestWhileIdle(dataSourceProperties.isTestWhileIdle());
        datasource.setTestOnBorrow(dataSourceProperties.isTestOnBorrow());
        datasource.setTestOnReturn(dataSourceProperties.isTestOnReturn());
        datasource.setPoolPreparedStatements(dataSourceProperties.isPoolPreparedStatements());
        datasource.setMaxPoolPreparedStatementPerConnectionSize(dataSourceProperties.getMaxPoolPreparedStatementPerConnectionSize());
        datasource.setFilters(dataSourceProperties.getFilters());
        datasource.setConnectionProperties(dataSourceProperties.getConnectionProperties());
        datasource.init();
        log.info("【soukuan spring boot component】 'DruidDataSource' init successful!");
        return datasource;
    }

    @Bean
    @ConditionalOnWebApplication
    @ConditionalOnProperty(prefix = DataSourceProperties.PREFIX, name = "servlet.enable", matchIfMissing = true)
    public ServletRegistrationBean druidStatViewServlet() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new DruidStatViewServlet());
        registration.addUrlMappings(dataSourceProperties.getServlet().getUrlPatterns());
        //白名单IP
        registration.addInitParameter("allow", dataSourceProperties.getServlet().getAllow());
        //黑名单IP
        registration.addInitParameter("deny", dataSourceProperties.getServlet().getDeny());
        //登录查看信息的账号密码.
        registration.addInitParameter("isNeedLogin", dataSourceProperties.getServlet().isNeedLogin() + "");
        registration.addInitParameter("loginUsername", dataSourceProperties.getServlet().getLoginUsername());
        registration.addInitParameter("loginPassword", dataSourceProperties.getServlet().getLoginPassword());
        //是否能够重置数据.
        registration.addInitParameter("resetEnable", dataSourceProperties.getServlet().isResetEnable() + "");
        //关闭session统计，用户每次访问IP不同报错
        registration.addInitParameter("sessionStatEnable", dataSourceProperties.getServlet().isSessionStatEnable() + "");
        log.info("【soukuan spring boot component】 'DruidDataSource servlet' init successful!");
        return registration;
    }

    @Bean
    @ConditionalOnWebApplication
    @ConditionalOnProperty(prefix = DataSourceProperties.PREFIX, name = "filter.enable", matchIfMissing = true)
    public FilterRegistrationBean druidStatFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setName(dataSourceProperties.getFilter().getFilterName());
        filterRegistrationBean.setUrlPatterns(Collections.singletonList(dataSourceProperties.getFilter().getUrlPatterns()));
        filterRegistrationBean.addInitParameter("exclusions", dataSourceProperties.getFilter().getExclusions());
        filterRegistrationBean.addInitParameter("sessionStatEnable", dataSourceProperties.getServlet().isSessionStatEnable() + "");
        filterRegistrationBean.setFilter(new DruidStatFilter());
        log.info("【soukuan spring boot component】 'DruidDataSource filter' init successful!");
        return filterRegistrationBean;
    }
}
