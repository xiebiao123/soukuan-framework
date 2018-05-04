package com.soukuan.properties;

import com.soukuan.util.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Title
 * Author jirenhe@wanshifu.com
 * Time 2017/5/31.
 * Version v1.0
 */
@ConfigurationProperties(prefix = DaoLogAopProperties.PREFIX)
public class DaoLogAopProperties {

    public static final String PREFIX = "soukuan.dao.log.aop";

    private static final String DEFAULT_POINTCUT = "execution(public * com.soukuan.mapper.*.*(..))";

    private boolean enable = true;

    private String daoLogPointcut;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getDaoLogPointcut() {
        return StringUtils.isNotEmpty(this.daoLogPointcut) ? this.daoLogPointcut : DEFAULT_POINTCUT;
    }

    public void setDaoLogPointcut(String daoLogPointcut) {
        this.daoLogPointcut = daoLogPointcut;
    }

}
