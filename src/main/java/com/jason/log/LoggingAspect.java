package com.jason.log;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("execution(* com.jason.controller..*(..))")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        log.info("[Method]: [" + joinPoint.getSignature().getName()+ "] begins");
        Arrays.stream(joinPoint.getArgs()).forEach(System.out::println);
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        // calling proceed() to start original method
        Object result = joinPoint.proceed();
        long spentTime = System.currentTimeMillis() - startTime;
        log.info("[Method]: [" + joinPoint.getSignature().getName() + "] end, Time spent: " + spentTime);
        return result;    	
    }
    
    // @After("pointcut()")
    // public void after(JoinPoint joinPoint) {
    //     System.out.println("=====After advice starts=====");

    //     log.info("visit /" + joinPoint.getSignature().getName());
    //     Arrays.stream(joinPoint.getArgs()).forEach(System.out::println);
        
    //     System.out.println("=====After advice ends=====");
    // }

    // @Around(value = "publicMethodsFromLoggingPackage()")
    // public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
    //     Object[] args = joinPoint.getArgs();
    //     String methodName = joinPoint.getSignature().getName();
    //     log.debug(">> {}() - {}", methodName, Arrays.toString(args));
    //     Object result = joinPoint.proceed();
    //     log.debug("<< {}() - {}", methodName, result);
    //     return result;
    // }
    
}
