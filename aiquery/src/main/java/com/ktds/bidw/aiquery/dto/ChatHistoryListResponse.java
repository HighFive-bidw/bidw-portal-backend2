package com.ktds.bidw.aiquery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 대화 히스토리 목록 응답 DTO 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "대화 히스토리 목록 응답")
public class ChatHistoryListResponse {

    @Schema(description = "대화 히스토리 목록")
    private List<ChatHistoryDTO> items;

    @Schema(description = "전체 아이템 수", example = "25")
    private int totalItems;

    @Schema(description = "전체 페이지 수", example = "3")
    private int totalPages;

    @Schema(description = "현재 페이지", example = "0")
    private int currentPage;
}