package com.ktds.bidw.aiquery.service;

import java.util.List;
import java.util.Map;

/**
 * AI API 클라이언트 서비스 인터페이스입니다.
 */
public interface AiClientService {

    /**
     * AI에 질문하여 응답을 받습니다.
     *
     * @param prompt 프롬프트
     * @param context 대화 컨텍스트
     * @param dataContext 데이터 컨텍스트
     * @return AI 응답
     */
    String askAi(String prompt, List<Map<String, String>> context, String dataContext);
}