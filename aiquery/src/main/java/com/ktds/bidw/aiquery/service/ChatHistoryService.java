package com.ktds.bidw.aiquery.service;

import com.ktds.bidw.aiquery.dto.ChatHistoryListResponse;
import com.ktds.bidw.aiquery.dto.ChatHistoryRequest;
import com.ktds.bidw.aiquery.dto.ChatHistoryResponse;

/**
 * 대화 히스토리 서비스 인터페이스입니다.
 */
public interface ChatHistoryService {

    /**
     * 대화 히스토리를 저장합니다.
     *
     * @param userId 사용자 ID
     * @param request 대화 히스토리 저장 요청 정보
     * @return 대화 히스토리 저장 결과
     */
    ChatHistoryResponse saveHistory(String userId, ChatHistoryRequest request);

    /**
     * 사용자의 대화 히스토리를 조회합니다.
     *
     * @param userId 사용자 ID
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 대화 히스토리 목록
     */
    ChatHistoryListResponse getHistory(String userId, int page, int size);
}