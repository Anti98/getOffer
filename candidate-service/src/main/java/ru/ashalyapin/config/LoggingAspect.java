package ru.ashalyapin.config;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@Profile("dev")
public class LoggingAspect {

    // 1. Контроллеры
    @Around("execution(* ru.ashalyapin.controller.*.*(..))")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        log.info("→ REST {} called", methodName);
        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - start;
            log.info("← REST {} returned ({} ms)", methodName, duration);
            return result;
        } catch (Exception e) {
            log.error("✗ REST {} failed after {} ms: {}", methodName, System.currentTimeMillis() - start, e.getMessage(), e);
            throw e;
        }
    }

    // 2. Сервисы (бизнес-логика)
    @Around("execution(* ru.ashalyapin.service..*.*(..))")
    public Object logService(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        log.info("▶ Service {} called with args: {}", methodName, joinPoint.getArgs());
        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - start;
            log.info("◀ Service {} returned: {} ({} ms)", methodName, result, duration);
            return result;
        } catch (Exception e) {
            log.error("✗ Service {} threw exception after {} ms: {}", methodName, System.currentTimeMillis() - start, e.getMessage(), e);
            throw e;
        }
    }

}