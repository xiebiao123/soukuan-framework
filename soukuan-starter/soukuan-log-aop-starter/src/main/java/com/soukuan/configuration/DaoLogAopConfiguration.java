package com.soukuan.configuration;

import com.soukuan.log.aops.DaoLogAspect;
import com.soukuan.properties.DaoLogAopProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Title
 * Time 2017/5/31.
 * Version v1.0
 */
@Configuration
@EnableConfigurationProperties(DaoLogAopProperties.class)
@ConditionalOnProperty(prefix = DaoLogAopProperties.PREFIX, name="enable" ,matchIfMissing = true)
public class DaoLogAopConfiguration extends AspectJExpressionPointcutAdvisor {

    private final Logger logger = LoggerFactory.getLogger(DaoLogAopConfiguration.class);

    @Autowired
    private DaoLogAopProperties DaoLogAopProperties;


    @PostConstruct
    public void init() {
        this.setExpression(DaoLogAopProperties.getDaoLogPointcut());
        this.setAdvice(new DaoLogAspect());
        logger.info("【 spring boot component】 'dao log aop' init successful!");
    }

}
