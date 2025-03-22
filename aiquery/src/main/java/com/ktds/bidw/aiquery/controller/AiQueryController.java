package com.ktds.bidw.aiquery.controller;

import com.ktds.bidw.aiquery.dto.*;
import com.ktds.bidw.aiquery.service.AiQueryService;
import com.ktds.bidw.aiquery.service.ChatHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AI 질의응답 관련 API 컨트롤러입니다.
 */
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Tag(name = "AI 질의응답 API", description = "리포트 데이터에 대한 AI 질의응답 API")
public class AiQueryController {

    private final AiQueryService aiQueryService;
    private final ChatHistoryService chatHistoryService;

    /**
     * 리포트 데이터에 대한 질문을 처리하고 AI 응답을 제공합니다.
     *
     * @param request 질문 요청 정보
     * @return AI 응답 정보
     */
    @PostMapping("/query")
    @Operation(summary = "AI 질의응답", description = "리포트 데이터에 대한 질문을 처리하고 AI 응답을 제공합니다.")
    public ResponseEntity<AiQueryResponse> queryAi(@RequestBody AiQueryRequest request) {
        AiQueryResponse response = aiQueryService.processQuery(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 사용자의 질의응답 히스토리를 저장합니다.
     *
     * @param request 대화 히스토리 저장 요청 정보
     * @return 대화 히스토리 저장 결과
     */
    @PostMapping("/history")
    @Operation(summary = "대화 히스토리 저장", description = "사용자의 질의응답 히스토리를 저장합니다.")
    public ResponseEntity<ChatHistoryResponse> saveHistory(
            @RequestParam String userId,
            @RequestBody ChatHistoryRequest request) {
        ChatHistoryResponse response = chatHistoryService.saveHistory(userId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 사용자의 질의응답 히스토리를 조회합니다.
     *
     * @param userId 사용자 ID
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 대화 히스토리 목록
     */
    @GetMapping("/history")
    @Operation(summary = "대화 히스토리 조회", description = "사용자의 질의응답 히스토리를 조회합니다.")
    public ResponseEntity<ChatHistoryListResponse> getHistory(
            @Parameter(description = "사용자 ID", required = true)
            @RequestParam String userId,
            @Parameter(description = "페이지 번호", required = false, example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", required = false, example = "10")
            @RequestParam(defaultValue = "10") int size) {
        ChatHistoryListResponse response = chatHistoryService.getHistory(userId, page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * 리포트 유형별 추천 질문 예시를 제공합니다.
     *
     * @param reportId 리포트 ID
     * @return 추천 질문 예시 목록
     */
    @GetMapping("/suggestions")
    @Operation(summary = "질문 예시 조회", description = "리포트 유형별 추천 질문 예시를 제공합니다.")
    public ResponseEntity<AiSuggestionListResponse> getSuggestions(
            @Parameter(description = "리포트 ID", required = true)
            @RequestParam Long reportId) {
        AiSuggestionListResponse response = aiQueryService.getSuggestions(reportId);
        return ResponseEntity.ok(response);
    }
}