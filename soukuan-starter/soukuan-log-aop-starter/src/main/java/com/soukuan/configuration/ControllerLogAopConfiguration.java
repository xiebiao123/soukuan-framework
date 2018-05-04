package com.soukuan.configuration;

import com.soukuan.log.aop.ControllerLogAspect;
import com.soukuan.properties.ControllerLogAopProperties;
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
@EnableConfigurationProperties(ControllerLogAopProperties.class)
@ConditionalOnProperty(prefix = ControllerLogAopProperties.PREFIX, name = "enable", matchIfMissing = true)
public class ControllerLogAopConfiguration extends AspectJExpressionPointcutAdvisor {

    private final Logger logger = LoggerFactory.getLogger(ControllerLogAopConfiguration.class);

    @Autowired
    private ControllerLogAopProperties ControllerLogAopProperties;


    @PostConstruct
    public void init() {
        this.setExpression(ControllerLogAopProperties.getControllerLogPointcut());
        this.setAdvice(new ControllerLogAspect());
        logger.info("【 spring boot component】 'controller log aop' init successful!");
    }

}
