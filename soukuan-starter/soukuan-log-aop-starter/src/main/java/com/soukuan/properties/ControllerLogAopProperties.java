package com.soukuan.properties;

import com.soukuan.util.StringUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Title
 * Author jirenhe@wanshifu.com
 * Time 2017/5/31.
 * Version v1.0
 */
@ConfigurationProperties(prefix = ControllerLogAopProperties.PREFIX)
public class ControllerLogAopProperties {

    public static final String PREFIX = "wanshifu.controllerLogAop";

    private static final String DEFAULT_POINTCUT = "execution(public * com.wanshifu.controller.*.*(..))";

    private boolean enable = true;

    private String controllerLogPointcut;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getControllerLogPointcut() {
        return StringUtils.isNotEmpty(this.controllerLogPointcut) ? this.controllerLogPointcut : DEFAULT_POINTCUT;
    }

    public void setControllerLogPointcut(String controllerLogPointcut) {
        this.controllerLogPointcut = controllerLogPointcut;
    }
}
