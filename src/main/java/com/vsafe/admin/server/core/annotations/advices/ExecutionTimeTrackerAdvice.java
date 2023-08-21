package com.vsafe.admin.server.core.annotations.advices;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ExecutionTimeTrackerAdvice {
    @Around("@annotation(com.vsafe.admin.server.core.annotations.TrackExecutionTime)")
    public Object trackTime(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.currentTimeMillis();
        log.info("Method name " + pjp.getSignature().toShortString() + " start");
        Object obj = pjp.proceed();
        long endTime = System.currentTimeMillis();
        log.info("Method name " + pjp.getSignature().toShortString() + " time taken to execute : " + (endTime - startTime) + " millis");
        return obj;
    }
}
