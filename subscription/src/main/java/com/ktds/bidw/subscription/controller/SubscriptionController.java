package com.ktds.bidw.subscription.controller;

import com.ktds.bidw.subscription.dto.SubscriptionListDTO;
import com.ktds.bidw.subscription.dto.SubscriptionRequest;
import com.ktds.bidw.subscription.dto.SubscriptionResponse;
import com.ktds.bidw.subscription.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 구독 관련 API 컨트롤러입니다.
 */
@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
@Tag(name = "구독 API", description = "리포트 구독 관리 API")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    
    /**
     * 사용자의 구독 리포트 목록을 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 구독 리포트 목록
     */
    @GetMapping("/list")
    @Operation(summary = "구독 리포트 목록 조회", description = "사용자가 구독한 리포트 목록을 조회합니다.")
    public ResponseEntity<List<SubscriptionListDTO>> getSubscriptionList(
            @Parameter(description = "사용자 ID", required = true)
            @RequestParam String userId) {
        List<SubscriptionListDTO> subscriptions = subscriptionService.getSubscriptionList(userId);
        return ResponseEntity.ok(subscriptions);
    }
    
    /**
     * 리포트를 구독합니다.
     *
     * @param userId 사용자 ID
     * @param request 구독 요청 정보
     * @return 구독 처리 결과
     */
    @PostMapping("/subscribe")
    @Operation(summary = "리포트 구독", description = "사용자가 리포트를 구독합니다.")
    public ResponseEntity<SubscriptionResponse> subscribeReport(
            @Parameter(description = "사용자 ID", required = true)
            @RequestParam String userId,
            @RequestBody SubscriptionRequest request) {
        SubscriptionResponse response = subscriptionService.subscribeReport(userId, request.getReportId());
        return ResponseEntity.ok(response);
    }
    
    /**
     * 리포트 구독을 취소합니다.
     *
     * @param subscriptionId 구독 ID
     * @param userId 사용자 ID
     * @return 구독 취소 처리 결과
     */
    @DeleteMapping("/{subscriptionId}")
    @Operation(summary = "리포트 구독 취소", description = "사용자가 구독 중인 리포트를 구독 취소합니다.")
    public ResponseEntity<SubscriptionResponse> unsubscribeReport(
            @Parameter(description = "구독 ID", required = true)
            @PathVariable Long subscriptionId,
            @Parameter(description = "사용자 ID", required = true)
            @RequestParam String userId) {
        SubscriptionResponse response = subscriptionService.unsubscribeReport(subscriptionId, userId);
        return ResponseEntity.ok(response);
    }
}
