package com.jie.aspect;


import com.alibaba.fastjson.JSON;
import com.jie.annotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;


@Component
@Aspect
@Slf4j
public class LogAspect {

    // 切断
    @Pointcut("@annotation(com.jie.annotation.SystemLog)")
    public void pointcut() {}

    // 环绕通知
    @Around("pointcut()")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {

        Object result;
        try {

            // 目标方法执行之前打印日志
             handleBefore(joinPoint);

            result = joinPoint.proceed();

            handleAfter(result);
            // 目标方法执行之后打印日志
        } finally {

            // 结束后换行
            // System.lineSeparator() 获取当前系统的换行符
            log.info("=======End=======" + System.lineSeparator());
        }

        return result;
    }

    private void handleAfter(Object result) {

        // 打印出参
        log.info("Response       : {}",JSON.toJSONString(result) );
    }

    private void handleBefore(ProceedingJoinPoint joinPoint) {

        ServletRequestAttributes servletRequest = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        // 获取当前请求
        HttpServletRequest request = servletRequest.getRequest();


        SystemLog systemLog = getSystemLog(joinPoint);

        Signature signature = joinPoint.getSignature();

        // joinPoint.getArgs() 获取请求的入参

        log.info("=======Start=======");
        // 打印请求 URL
        log.info("URL            : {}",request.getRequestURL());
        // 打印描述信息
        log.info("BusinessName   : {}", systemLog.businessName());
        // 打印 Http method
        log.info("HTTP Method    : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method   : {}.{}", signature.getDeclaringTypeName(),((MethodSignature)joinPoint.getSignature()).getName());
        // 打印请求的 IP
        log.info("IP             : {}",request.getRemoteAddr());
        // 打印请求入参
        log.info("Request Args   : {}", JSON.toJSONString(joinPoint.getArgs()));
    }

    /**
     * 获取当前系统日志的注解对象
     * @param joinPoint
     * @return
     */
    private SystemLog getSystemLog(ProceedingJoinPoint joinPoint) {

        // 获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        //再获取方法
        Method method = signature.getMethod();
        // 根据方法对象获取注解


        return method.getAnnotation(SystemLog.class);
    }

}
