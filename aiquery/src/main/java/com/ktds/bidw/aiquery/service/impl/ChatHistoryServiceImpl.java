package com.ktds.bidw.aiquery.service.impl;

import com.ktds.bidw.aiquery.domain.ChatHistory;
import com.ktds.bidw.aiquery.dto.ChatHistoryDTO;
import com.ktds.bidw.aiquery.dto.ChatHistoryListResponse;
import com.ktds.bidw.aiquery.dto.ChatHistoryRequest;
import com.ktds.bidw.aiquery.dto.ChatHistoryResponse;
import com.ktds.bidw.aiquery.repository.ChatHistoryRepository;
import com.ktds.bidw.aiquery.service.ChatHistoryService;
import com.ktds.bidw.aiquery.service.ReportClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

/**
 * 대화 히스토리 서비스 구현 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class ChatHistoryServiceImpl implements ChatHistoryService {

    private final ChatHistoryRepository chatHistoryRepository;
    private final ReportClient reportClient;

    /**
     * 대화 히스토리를 저장합니다.
     *
     * @param userId 사용자 ID
     * @param request 대화 히스토리 저장 요청 정보
     * @return 대화 히스토리 저장 결과
     */
    @Override
    @Transactional
    public ChatHistoryResponse saveHistory(String userId, ChatHistoryRequest request) {
        // 리포트 정보 조회
        String reportName = reportClient.getReportDetail(request.getReportId()).getReportName();

        // 대화 히스토리 저장
        ChatHistory chatHistory = new ChatHistory(
                null,
                userId,
                request.getReportId(),
                reportName,
                request.getQuestion(),
                request.getAnswer(),
                null
        );

        ChatHistory savedHistory = chatHistoryRepository.save(chatHistory);

        return new ChatHistoryResponse("대화 히스토리가 저장되었습니다.", savedHistory.getId());
    }

    /**
     * 사용자의 대화 히스토리를 조회합니다.
     *
     * @param userId 사용자 ID
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 대화 히스토리 목록
     */
    @Override
    @Transactional(readOnly = true)
    public ChatHistoryListResponse getHistory(String userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ChatHistory> historyPage = chatHistoryRepository.findByUserId(userId, pageRequest);

        return new ChatHistoryListResponse(
                historyPage.getContent().stream()
                        .map(this::convertToChatHistoryDTO)
                        .collect(Collectors.toList()),
                (int) historyPage.getTotalElements(),
                historyPage.getTotalPages(),
                historyPage.getNumber()
        );
    }

    /**
     * ChatHistory를 ChatHistoryDTO로 변환합니다.
     *
     * @param chatHistory ChatHistory 객체
     * @return ChatHistoryDTO 객체
     */
    private ChatHistoryDTO convertToChatHistoryDTO(ChatHistory chatHistory) {
        return new ChatHistoryDTO(
                chatHistory.getId(),
                chatHistory.getReportId(),
                chatHistory.getReportName(),
                chatHistory.getQuestion(),
                chatHistory.getAnswer(),
                chatHistory.getCreatedAt()
        );
    }
}