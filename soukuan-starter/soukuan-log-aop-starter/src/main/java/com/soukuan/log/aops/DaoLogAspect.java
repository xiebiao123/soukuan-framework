package com.soukuan.log.aop;

import com.alibaba.fastjson.JSON;
import com.soukuan.util.StringUtils;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;


/**
 * Title Dao层日志切面
 * DateTime  17-4-21.
 * Version V1.0.0
 */
public class DaoLogAspect implements MethodInterceptor{

    private final Logger logger = LoggerFactory.getLogger("[mapper ]");

    private final static int LOG_RET_STR_LEN = 100;

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Method method = methodInvocation.getMethod();
        String methodName = method.getName();
        String className = method.getDeclaringClass().getSimpleName();
        if (logger.isDebugEnabled()) {
            logger.debug(" #S# {}.{} with args= {} ...",className, methodName, Arrays.toString(methodInvocation.getArguments()));
        } else {
            logger.info(" #S# {}.{} ...", className, methodName);
        }
        long beginTime = System.currentTimeMillis();
        Object retVal = methodInvocation.proceed();
        long endTime = System.currentTimeMillis();
        logger.info(" #T# {}.{} TAKE TIME {} ms", className ,methodName, (endTime - beginTime));

        //json2obj是非常耗时的操作，所以生产环境不能执行此代码块
        if (logger.isDebugEnabled()) {
            String retValStr = JSON.toJSONString(retVal);
            String size = "";
            if (List.class.isInstance(retVal)) {
                size = "(" + ((List) retVal).size() + ")";
            }
            logger.debug(" #E# {} with ret{}= {}", methodName, size, StringUtils.abbr(retValStr, LOG_RET_STR_LEN));
        }
        return retVal;
    }
}
