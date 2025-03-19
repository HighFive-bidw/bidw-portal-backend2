package com.ktds.bidw.common.filter;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * API 요청에 대한 메트릭을 수집하는 필터입니다.
 */
public class MetricsFilter extends OncePerRequestFilter {

    private final Counter apiGatewayRequestsCounter;

    public MetricsFilter(MeterRegistry meterRegistry) {
        this.apiGatewayRequestsCounter = Counter.builder("api_gateway_requests_total")
                .description("API Gateway를 통한 총 요청 수")
                .register(meterRegistry);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // API 요청 카운터 증가
        apiGatewayRequestsCounter.increment();

        // 필터 체인 계속 진행
        filterChain.doFilter(request, response);
    }
}
