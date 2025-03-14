package com.ktds.bidw.common.util;

import com.ktds.bidw.common.exception.BusinessException;

import java.time.LocalDate;

/**
 * 유효성 검사 유틸리티 클래스입니다.
 */
public class ValidationUtils {
    
    /**
     * 시작 날짜와 종료 날짜의 유효성을 검사합니다.
     *
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @throws BusinessException 날짜가 유효하지 않을 경우
     */
    public static void validateStartEndDates(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new BusinessException("VALIDATION_ERROR", "시작일과 종료일은 필수 입력 항목입니다.");
        }
        
        if (startDate.isAfter(endDate)) {
            throw new BusinessException("VALIDATION_ERROR", "시작일은 종료일보다 이전이어야 합니다.");
        }
        
        if (endDate.isAfter(LocalDate.now())) {
            throw new BusinessException("VALIDATION_ERROR", "종료일은 오늘 이후의 날짜일 수 없습니다.");
        }
    }
}
