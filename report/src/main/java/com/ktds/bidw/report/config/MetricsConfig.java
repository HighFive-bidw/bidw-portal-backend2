package com.ktds.bidw.report.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;


/**
 * 메트릭 설정 클래스입니다.
 * 시스템 전반에서 사용할 메트릭을 정의합니다.
 */
@Configuration
public class MetricsConfig {

    @Value("${spring.application.name:unknown}")
    private String applicationName;

    // API Gateway 요청 메트릭
    @Bean
    public Counter apiGatewayRequestsCounter(MeterRegistry registry) {
        return Counter.builder("api_gateway_requests_total")
                .description("API Gateway를 통한 총 요청 수")
                .tag("service", applicationName)  // 서비스 구분을 위한 태그 추가
                .register(registry);
    }

    // Excel 파일 저장 시간 측정 타이머
    @Bean
    public Timer excelFileStorageTimer(MeterRegistry registry) {
        return Timer.builder("excel_file_storage_time_seconds")
                .description("Excel 파일 저장 소요 시간")
                .tag("service", applicationName)  // 서비스 구분을 위한 태그 추가
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .sla(
                        Duration.ofMillis(50),
                        Duration.ofMillis(100),
                        Duration.ofMillis(200),
                        Duration.ofMillis(500)
                )
                .register(registry);
    }



    // HTTP 계층 메트릭 (컨트롤러용)
    @Bean
    public Timer reportDownloadTimer(MeterRegistry registry) {
        return Timer.builder("report_download_seconds")
                .description("리포트 다운로드 처리 소요 시간")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .sla(
                        Duration.ofMillis(500),
                        Duration.ofSeconds(1),
                        Duration.ofSeconds(2),
                        Duration.ofSeconds(5)
                )
                .register(registry);
    }

    @Bean
    public Counter reportDownloadTotalCounter(MeterRegistry registry) {
        return Counter.builder("report_download_total")
                .description("전체 리포트 다운로드 요청 수")
                .register(registry);
    }

    @Bean
    public Counter reportDownloadSuccessCounter(MeterRegistry registry) {
        return Counter.builder("report_download_success_total")
                .description("성공한 리포트 다운로드 수")
                .register(registry);
    }

    @Bean
    public Counter reportDownloadFailureCounter(MeterRegistry registry) {
        return Counter.builder("report_download_failure_total")
                .description("실패한 리포트 다운로드 수")
                .register(registry);
    }

    // HTTP 계층 전용 메트릭
    @Bean
    public Timer httpReportDownloadTimer(MeterRegistry registry) {
        return Timer.builder("http_report_download_seconds")
                .description("HTTP 리포트 다운로드 처리 시간")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(registry);
    }

    // Valet Key 패턴 관련 메트릭
    @Bean
    public Timer sasTokenGenerationTimer(MeterRegistry registry) {
        return Timer.builder("azure_storage_sas_generation_seconds")
                .description("SAS 토큰 생성 소요 시간")
                .tag("service", applicationName)  // 서비스 구분을 위한 태그 추가
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .sla(
                        Duration.ofMillis(10),
                        Duration.ofMillis(50),
                        Duration.ofMillis(100),
                        Duration.ofMillis(200)
                )
                .register(registry);
    }

    @Bean
    public Counter sasTokenIssuedCounter(MeterRegistry registry) {
        return Counter.builder("azure_storage_sas_issued_total")
                .description("발급된 SAS 토큰 수")
                .register(registry);
    }

    // Claim Check 패턴 관련 메트릭
    @Bean
    public Timer excelGenerationTimer(MeterRegistry registry) {
        return Timer.builder("excel_generation_seconds")
                .description("Excel 파일 생성 소요 시간")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .sla(
                        Duration.ofMillis(200),
                        Duration.ofMillis(500),
                        Duration.ofSeconds(1),
                        Duration.ofSeconds(2)
                )
                .register(registry);
    }

    @Bean
    public Timer fileStorageTimer(MeterRegistry registry) {
        return Timer.builder("file.storage.operations")
                .description("Timer for file storage operations")
                .register(registry);
    }

    @Bean
    public Timer blobStorageOperationTimer(MeterRegistry registry) {
        return Timer.builder("azure_storage_blob_operation_seconds")
                .description("Azure Blob Storage 작업 소요 시간")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .sla(
                        Duration.ofMillis(50),
                        Duration.ofMillis(100),
                        Duration.ofMillis(200),
                        Duration.ofMillis(500)
                )
                .register(registry);
    }

    @Bean
    public Counter excelFileSizeCounter(MeterRegistry registry) {
        return Counter.builder("excel_file_size_bytes_total")
                .description("생성된 Excel 파일 크기 합계")
                .tag("service", applicationName)  // 서비스 구분을 위한 태그 추가
                .register(registry);
    }
}