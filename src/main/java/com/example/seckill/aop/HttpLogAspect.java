package com.example.seckill.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class HttpLogAspect {

    @Around("execution(* com.example.seckill.controller..*(..))")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        log.info("[AOP START] Method: {} Execution Args: {}",
                joinPoint.getSignature().toShortString(), Arrays.toString(joinPoint.getArgs()));

        Object result = joinPoint.proceed();

        log.info("[AOP END] Method executed in {} ms. Result: {}",
                (System.currentTimeMillis() - start), result);
        return result;
    }
}