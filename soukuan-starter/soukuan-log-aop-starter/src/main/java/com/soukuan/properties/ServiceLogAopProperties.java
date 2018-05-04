package com.soukuan.properties;

import com.soukuan.util.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Title
 * Time 2017/5/31.
 * Version v1.0
 */
@ConfigurationProperties(prefix = ServiceLogAopProperties.PREFIX)
public class ServiceLogAopProperties {

    public static final String PREFIX = "soukuan.service.log.aop";

    private static final String DEFAULT_POINTCUT = "execution(public * com.soukuan.service.impl.*.*(..))";

    private String serviceLogPointcut;

    private boolean enable = true;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getServiceLogPointcut() {
        return StringUtils.isNotEmpty(this.serviceLogPointcut) ? this.serviceLogPointcut : DEFAULT_POINTCUT;
    }

    public void setServiceLogPointcut(String serviceLogPointcut) {
        this.serviceLogPointcut = serviceLogPointcut;
    }
}
