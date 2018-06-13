package com.soukuan.config;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.soukuan.properties.GlobalParameter;
import com.soukuan.properties.SwaggerConfigProperties;
import com.soukuan.util.CollectionUtils;
import com.soukuan.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Title
 * Author xiebiao@soukuan.com
 * Time 2017/11/13.
 * Version v1.0
 */
@EnableSwagger2
@Configuration
@EnableConfigurationProperties(SwaggerConfigProperties.class)
@Import({springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration.class})
@ConditionalOnProperty(prefix = SwaggerConfigProperties.PREFIX, name = "enable", matchIfMissing = true)
public class SwaggerConfiguration {

    @Autowired
    private TypeResolver typeResolver;

    @Autowired
    private SwaggerConfigProperties swaggerConfigProperties;

    @Bean
    public Docket petApi() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .select()
                //扫描的包路径
                .apis(RequestHandlerSelectors.any())
                //扫描的URL
                .paths(paths())
                .build()
                .pathMapping("/")
                //类型的转化
                .directModelSubstitute(LocalDate.class,String.class)
                .genericModelSubstitutes(ResponseEntity.class)
                .alternateTypeRules(AlternateTypeRules.newRule(typeResolver.resolve(DeferredResult.class,
                                typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
                                typeResolver.resolve(WildcardType.class)))
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.POST,
                        Lists.newArrayList(new ResponseMessageBuilder()
                                .code(200)
                                .responseModel(new ModelRef("ResponseEntity"))
                                .build()))
                .enableUrlTemplating(false)
                .apiInfo(apiInfo())
                .tags(new Tag("Pet Service", "All apis relating to pets"));

        //更改请求的host
        if(StringUtils.isNotEmpty(swaggerConfigProperties.getHost())){
            docket.host(swaggerConfigProperties.getHost());
        }
        //为所有操作添加默认参数
        if(CollectionUtils.isNotEmpty(swaggerConfigProperties.getGlobalOperationParameters())){
            List<Parameter> parameters = new ArrayList<>(swaggerConfigProperties.getGlobalOperationParameters().size());
            for (GlobalParameter globalParameter : swaggerConfigProperties.getGlobalOperationParameters()) {
                parameters.add(new ParameterBuilder()
                        .name(globalParameter.getName())
                        .description(globalParameter.getDescription())
                        .modelRef(new ModelRef(globalParameter.getModelRef()))
                        .parameterType(globalParameter.getParameterType())
                        .required(globalParameter.isRequire())
                        .defaultValue(globalParameter.getDefaultValue())
                        .build());
            }
            docket.globalOperationParameters(parameters);
        }
        return docket;
    }

    private Predicate<String> paths() {
        if(CollectionUtils.isNotEmpty(swaggerConfigProperties.getAllowPaths())){
            Predicate<String>[] predicates = new Predicate[swaggerConfigProperties.getAllowPaths().size()];
            for (int i = 0; i < swaggerConfigProperties.getAllowPaths().size(); i++) {
                predicates[i] = PathSelectors.regex(swaggerConfigProperties.getAllowPaths().get(i));
            }
            return Predicates.or(predicates);
        }else{
            return PathSelectors.any();
        }
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact(swaggerConfigProperties.getAuthor(), "", swaggerConfigProperties.getEmail());
        return new ApiInfo(swaggerConfigProperties.getTitle(),//大标题 title
                swaggerConfigProperties.getDescription(),//小标题
                swaggerConfigProperties.getVersion(),//版本
                swaggerConfigProperties.getUrl(),//termsOfServiceUrl
                contact,//作者
                swaggerConfigProperties.getLicense(),//链接显示文字
                swaggerConfigProperties.getLicenseUrl(),//网站链接
                Collections.emptyList()
        );
    }

    @Bean
    public UiConfiguration uiConfig() {
        return new UiConfiguration(
                "validatorUrl",// url
                "none",       // docExpansion          => none | list
                "alpha",      // apiSorter             => alpha
                "schema",     // defaultModelRendering => schema
                UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS,
                true,        // enableJsonEditor      => true | false
                true,         // showRequestHeaders    => true | false
                60000L);      // requestTimeout => in milliseconds, defaults to null (uses jquery xh timeout)
    }
}
