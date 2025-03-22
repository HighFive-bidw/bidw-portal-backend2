package com.ktds.bidw.aiquery.service;

import com.ktds.bidw.aiquery.dto.AiQueryRequest;
import com.ktds.bidw.aiquery.dto.AiQueryResponse;
import com.ktds.bidw.aiquery.dto.AiSuggestionListResponse;

/**
 * AI 질의응답 서비스 인터페이스입니다.
 */
public interface AiQueryService {

    /**
     * 사용자 질문을 처리하고 AI 응답을 제공합니다.
     *
     * @param request 질문 요청 정보
     * @return AI 응답 정보
     */
    AiQueryResponse processQuery(AiQueryRequest request);

    /**
     * 리포트 유형별 추천 질문 예시를 제공합니다.
     *
     * @param reportId 리포트 ID
     * @return 추천 질문 예시 목록
     */
    AiSuggestionListResponse getSuggestions(Long reportId);
}