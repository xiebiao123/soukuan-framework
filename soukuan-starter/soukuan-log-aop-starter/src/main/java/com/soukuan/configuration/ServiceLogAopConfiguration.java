import com.soukuan.log.aop.ServiceLogAspect;
import com.soukuan.properties.ServiceLogAopProperties;
import org.aopalliance.aop.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.annotation.PostConstruct;

/**
 * Title
 * Author jirenhe@.com
 * Time 2017/5/31.
 * Version v1.0
 */
@Configuration
@ConditionalOnClass({ EnableAspectJAutoProxy.class, Advice.class })
@EnableConfigurationProperties(ServiceLogAopProperties.class)
@ConditionalOnProperty(prefix = ServiceLogAopProperties.PREFIX, name="enable" ,matchIfMissing = true)
public class ServiceLogAopConfiguration extends AspectJExpressionPointcutAdvisor {

    private final Logger logger = LoggerFactory.getLogger(ServiceLogAopConfiguration.class);

    @Autowired
    private ServiceLogAopProperties ServiceLogAopProperties;


    @PostConstruct
    public void init() {
        this.setExpression(ServiceLogAopProperties.getServiceLogPointcut());
        this.setAdvice(new ServiceLogAspect());
        logger.info("【 spring boot component】 'service log aop' init successful!");
    }

}
