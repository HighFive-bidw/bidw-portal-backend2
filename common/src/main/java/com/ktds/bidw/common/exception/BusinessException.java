package com.ktds.bidw.common.exception;

import lombok.Getter;

/**
 * 비즈니스 로직 처리 중 발생하는 예외 클래스입니다.
 */
@Getter
public class BusinessException extends RuntimeException {
    
    private final String errorCode;
    
    /**
     * 오류 메시지를 받는 생성자입니다.
     *
     * @param message 오류 메시지
     */
    public BusinessException(String message) {
        this(ErrorCode.BUSINESS_ERROR, message);
    }
    
    /**
     * 오류 메시지와 원인 예외를 받는 생성자입니다.
     *
     * @param message 오류 메시지
     * @param cause 원인 예외
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = ErrorCode.BUSINESS_ERROR;
    }
    
    /**
     * 오류 코드와 메시지를 받는 생성자입니다.
     *
     * @param errorCode 오류 코드
     * @param message 오류 메시지
     */
    public BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
