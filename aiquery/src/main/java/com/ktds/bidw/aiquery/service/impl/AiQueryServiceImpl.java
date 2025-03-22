package com.ktds.bidw.aiquery.service.impl;

import com.ktds.bidw.aiquery.domain.Conversation;
import com.ktds.bidw.aiquery.domain.Message;
import com.ktds.bidw.aiquery.dto.*;
import com.ktds.bidw.aiquery.service.AiClientService;
import com.ktds.bidw.aiquery.service.AiQueryService;
import com.ktds.bidw.aiquery.service.ConversationService;
import com.ktds.bidw.aiquery.service.ReportClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * AI 질의응답 서비스 구현 클래스입니다.
 * 오류 발생 문제를 해결하기 위해 단순화된 버전입니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiQueryServiceImpl implements AiQueryService {

    private final AiClientService aiClientService;
    private final ConversationService conversationService;
    private final ReportClient reportClient;

    /**
     * 사용자 질문을 처리하고 AI 응답을 제공합니다.
     */
    @Override
    public AiQueryResponse processQuery(AiQueryRequest request) {
        try {
            // 리포트 데이터 가져오기
            ReportDetailResponse reportDetail = null;
            try {
                reportDetail = reportClient.getReportDetail(request.getReportId());
            } catch (Exception e) {
                log.error("리포트 데이터 조회 중 오류 발생: {}", e.getMessage());
                reportDetail = new ReportDetailResponse(request.getReportId(), "리포트 정보 없음", null, Collections.emptyList());
            }

            // 데이터 컨텍스트 생성
            String dataContext = createSimpleDataContext(reportDetail);

            // 대화 컨텍스트 관리
            Conversation conversation = null;
            List<Map<String, String>> conversationContext = new ArrayList<>();

            try {
                if (request.getConversationId() != null && !request.getConversationId().isEmpty()) {
                    conversation = conversationService.getConversation(request.getConversationId());
                } else {
                    conversation = conversationService.createConversation(request.getUserId(), request.getReportId());
                }
            } catch (Exception e) {
                log.error("대화 컨텍스트 관리 중 오류 발생: {}", e.getMessage());
                // 임시 대화 객체 생성
                conversation = new Conversation("temp-" + UUID.randomUUID().toString(), request.getUserId(), request.getReportId());
            }

            // AI 질의
            String answer = aiClientService.askAi(request.getQuestion(), conversationContext, dataContext);

            // 대화 컨텍스트 업데이트 시도
            try {
                conversation.addMessage(new Message(Message.Role.USER, request.getQuestion()));
                conversation.addMessage(new Message(Message.Role.AI, answer));
                conversationService.updateConversation(conversation);
            } catch (Exception e) {
                log.error("대화 컨텍스트 업데이트 중 오류 발생: {}", e.getMessage());
            }

            // 응답 생성
            return new AiQueryResponse(answer, conversation.getId(), Collections.emptyList(), Collections.emptyList());

        } catch (Exception e) {
            log.error("AI 쿼리 처리 중 오류 발생", e);

            // 오류 발생 시 기본 응답 제공
            return new AiQueryResponse(
                    "죄송합니다. 요청을 처리하는 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.",
                    request.getConversationId() != null ? request.getConversationId() : "error-" + System.currentTimeMillis(),
                    Collections.emptyList(),
                    Collections.emptyList()
            );
        }
    }

    /**
     * 리포트 유형별 추천 질문 예시를 제공합니다.
     */
    @Override
    public AiSuggestionListResponse getSuggestions(Long reportId) {
        List<String> suggestions = new ArrayList<>();

        suggestions.add("이 리포트의 주요 인사이트는 무엇인가요?");
        suggestions.add("데이터에서 어떤 패턴이나 추세를 발견할 수 있나요?");
        suggestions.add("이 리포트의 요약을 해주세요.");
        suggestions.add("이 데이터에서 가장 중요한 정보는 무엇인가요?");
        suggestions.add("이 리포트를 기반으로 어떤 의사결정을 내릴 수 있나요?");

        return new AiSuggestionListResponse(suggestions);
    }

    /**
     * 간단한 데이터 컨텍스트 생성
     */
    private String createSimpleDataContext(ReportDetailResponse reportDetail) {
        StringBuilder context = new StringBuilder();
        context.append("리포트 이름: ").append(reportDetail.getReportName()).append("\n");

        if (reportDetail.getLastUpdated() != null) {
            context.append("마지막 업데이트: ").append(reportDetail.getLastUpdated()).append("\n\n");
        }

        return context.toString();
    }
}