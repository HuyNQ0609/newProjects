package com.vsafe.admin.server.core.annotations.advices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAdvice {
    @Around("@annotation(com.vsafe.admin.server.core.annotations.Logging)")
    public Object applicationLogger(ProceedingJoinPoint pjp) throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String methodName = pjp.getSignature().getName();
        String className = pjp.getTarget().getClass().toString();
        Object[] array = pjp.getArgs();
        log.info("Method invoked " + className + " : " + methodName + "(...)" + " - arguments : "
                + mapper.writeValueAsString(array));
        Object object = pjp.proceed();
        log.info(className + " : " + methodName + "(...)" + " Response : "
                + mapper.writeValueAsString(object));
        return object;
    }
}
