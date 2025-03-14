package com.ktds.bidw.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 로깅을 위한 AOP 클래스입니다.
 */
@Slf4j
@Aspect
@Component
public class LoggingAspect {
    
    /**
     * 컨트롤러 메서드 실행 시작 시 로깅합니다.
     *
     * @param joinPoint 조인 포인트
     */
    @Before("execution(* com.ktds.bidw.*.controller.*.*(..))")
    public void logMethodStart(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();
        
        log.info("==> [{}] {}.{}() started with arguments: {}", getRequestId(), className, methodName, Arrays.toString(args));
    }
    
    /**
     * 컨트롤러 메서드 실행 종료 시 로깅합니다.
     *
     * @param joinPoint 조인 포인트
     * @param result 실행 결과
     */
    @AfterReturning(pointcut = "execution(* com.ktds.bidw.*.controller.*.*(..))", returning = "result")
    public void logMethodEnd(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        log.info("<== [{}] {}.{}() returned with: {}", getRequestId(), className, methodName, result);
    }
    
    /**
     * 컨트롤러 메서드 실행 중 예외 발생 시 로깅합니다.
     *
     * @param joinPoint 조인 포인트
     * @param ex 발생한 예외
     */
    @AfterThrowing(pointcut = "execution(* com.ktds.bidw.*.controller.*.*(..))", throwing = "ex")
    public void logMethodException(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        log.error("!!! [{}] {}.{}() threw exception: {}", getRequestId(), className, methodName, ex.getMessage(), ex);
    }
    
    /**
     * 요청 ID를 생성합니다.
     *
     * @return 요청 ID
     */
    private String getRequestId() {
        return String.format("%08X", System.currentTimeMillis() & 0xFFFFFFFF);
    }
}
