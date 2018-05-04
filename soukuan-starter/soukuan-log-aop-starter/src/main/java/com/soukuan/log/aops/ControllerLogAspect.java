package com.soukuan.log.aop;

import com.alibaba.fastjson.JSON;
import com.soukuan.util.StringUtils;
import com.soukuan.util.web.RequestUtil;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Title
 * Time 2017/7/18.
 * Version v1.0
 */
public class ControllerLogAspect implements MethodInterceptor {

    private final Logger logger = LoggerFactory.getLogger(ControllerLogAspect.class);

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        if (logger.isDebugEnabled()) {
            Map<String, String[]> paramsMap = request.getParameterMap();
            String paramsJsonStr = JSON.toJSONString(paramsMap);
            logger.debug("##BEGIN-REQUEST## ：address: [{}] requestUrl: [{}] params: [{}] arguments: [{}]",
                    RequestUtil.getRemoteAddr(request), request.getRequestURI(), paramsJsonStr, methodInvocation.getArguments());
        } else {
            logger.info("##BEGIN-REQUEST## ：address: [{}] requestUrl: [{}] arguments: [{}]",
                    RequestUtil.getRemoteAddr(request), request.getRequestURI(), methodInvocation.getArguments());
        }
        long beginTime = System.currentTimeMillis();
        Object retVal = methodInvocation.proceed();
        long endTime = System.currentTimeMillis();
        String retValJson = JSON.toJSONString(retVal);
        logger.info("##REQUEST-END## ：address: [{}] requestURI: [{}] take times : [{}] return : [{}]",
                RequestUtil.getRemoteAddr(request), request.getRequestURI(), endTime - beginTime, StringUtils.abbr(retValJson, 5000));
        return retVal;
    }
}
