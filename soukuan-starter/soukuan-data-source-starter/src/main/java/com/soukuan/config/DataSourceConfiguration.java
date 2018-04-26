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
 * Author jirenhe@wanshifu.com
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
        registration.addInitParameter("allow", dataSourceProperties.getServlet().getAllow());
        registration.addInitParameter("deny", dataSourceProperties.getServlet().getDeny());
        registration.addInitParameter("loginUsername", dataSourceProperties.getServlet().getLoginUsername());
        registration.addInitParameter("loginPassword", dataSourceProperties.getServlet().getLoginPassword());
        registration.addInitParameter("resetEnable", dataSourceProperties.getServlet().isResetEnable() + "");
        registration.addInitParameter("sessionStatEnable", dataSourceProperties.getServlet().isSessionStatEnable() + "");
        registration.addInitParameter("isNeedLogin", dataSourceProperties.getServlet().isNeedLogin() + "");
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
