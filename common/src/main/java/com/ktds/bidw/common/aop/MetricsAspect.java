package com.ktds.bidw.common.aop;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 메트릭 수집을 위한 Aspect 클래스입니다.
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class MetricsAspect {

    private final MeterRegistry meterRegistry;

    /**
     * ReportController의 다운로드 API에 대한 메트릭을 수집합니다.
     */
    @Around("execution(* com.ktds.bidw.report.controller.ReportController.downloadReport(..))")
    public Object recordReportDownloadMetrics(ProceedingJoinPoint joinPoint) throws Throwable {
        Timer.Sample sample = Timer.start(meterRegistry);
        boolean success = false;

        try {
            Object result = joinPoint.proceed();
            success = true;
            return result;
        } finally {
            String outcome = success ? "success" : "failure";
            sample.stop(Timer.builder("http_report_download_seconds")
                    .tag("outcome", outcome)
                    .register(meterRegistry));

            Counter counter = Counter.builder("http_report_download_" + outcome + "_total")
                    .register(meterRegistry);
            counter.increment();
        }
    }

    /**
     * Excel 파일 생성에 대한 메트릭을 수집합니다.
     */
    @Around("execution(* com.ktds.bidw.report.service.ExcelServiceImpl.generateExcel(..))")
    public Object recordExcelGenerationMetrics(ProceedingJoinPoint joinPoint) throws Throwable {
        Timer.Sample sample = Timer.start(meterRegistry);
        boolean success = false;

        try {
            Object result = joinPoint.proceed();
            success = true;
            return result;
        } finally {
            String outcome = success ? "success" : "failure";
            sample.stop(Timer.builder("excel_file_generation_seconds")
                    .tag("outcome", outcome)
                    .register(meterRegistry));
        }
    }

    /**
     * Azure Blob Storage 작업에 대한 메트릭을 수집합니다.
     */
    @Around("execution(* com.ktds.bidw.report.service.FileStorageImpl.storeFile(..)) || execution(* com.ktds.bidw.report.service.FileStorageImpl.generateSignedUrl(..))")
    public Object recordStorageOperationMetrics(ProceedingJoinPoint joinPoint) throws Throwable {
        Timer.Sample sample = Timer.start(meterRegistry);
        boolean success = false;
        String methodName = joinPoint.getSignature().getName();

        try {
            Object result = joinPoint.proceed();
            success = true;
            return result;
        } finally {
            String outcome = success ? "success" : "failure";
            sample.stop(Timer.builder("azure_storage_operation_seconds")
                    .tag("method", methodName)
                    .tag("outcome", outcome)
                    .register(meterRegistry));
        }
    }
}
