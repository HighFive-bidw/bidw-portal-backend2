package com.ktds.bidw.common.exception;

/**
 * 오류 코드 상수 클래스입니다.
 */
public class ErrorCode {
    
    public static final String INVALID_CREDENTIALS = "INVALID_CREDENTIALS";
    public static final String UNAUTHORIZED = "UNAUTHORIZED";
    public static final String RESOURCE_NOT_FOUND = "RESOURCE_NOT_FOUND";
    public static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    public static final String BUSINESS_ERROR = "BUSINESS_ERROR";
    public static final String SYSTEM_ERROR = "SYSTEM_ERROR";
    
    private ErrorCode() {
        // 상수 클래스이므로 인스턴스화 방지
    }
}
