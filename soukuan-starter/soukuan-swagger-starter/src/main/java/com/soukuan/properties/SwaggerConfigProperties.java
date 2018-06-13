package com.soukuan.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Title
 * Author xiebiao@soukuan.com
 * Time 2017/5/31.
 * Version v1.0
 */
@ConfigurationProperties(prefix = SwaggerConfigProperties.PREFIX)
@Data
public class SwaggerConfigProperties {

    public static final String PREFIX = "soukuan.swagger";

    /**
     * 是否开启Swagger，默认关闭
     */
    private boolean enable = true;

    /**
     * 作者，默认soukuan
     */
    private String author = "soukuan";

    /**
     * 作者邮箱地址
     */
    private String email;

    /**
     * 文档标题
     */
    private String title = "未设置";

    /**
     * 文档描述
     */
    private String description = "未设置";

    /**
     * 版本号
     */
    private String version = "1.0.0";

    /**
     * 官方地址
     */
    private String url = "www.soukuan.com";

    /**
     * license
     */
    private String license = "搜款式";

    /**
     * licenseUrl
     */
    private String licenseUrl = "http://www.soukuan.com";

    /**
     * 允许访问的路径
     */
    private List<String> allowPaths;

    /**
     * host
     */
    private String host;

    /**
     * 定义全局参数，
     */
    private List<GlobalParameter> globalOperationParameters;

}
