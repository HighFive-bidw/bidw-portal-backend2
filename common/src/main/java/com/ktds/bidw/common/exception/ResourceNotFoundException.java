package com.ktds.bidw.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 리소스를 찾을 수 없을 때 발생하는 예외 클래스입니다.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends BusinessException {
    
    /**
     * 오류 메시지를 받는 생성자입니다.
     *
     * @param message 오류 메시지
     */
    public ResourceNotFoundException(String message) {
        super(ErrorCode.RESOURCE_NOT_FOUND, message);
    }
    
    /**
     * 오류 메시지와 원인 예외를 받는 생성자입니다.
     *
     * @param message 오류 메시지
     * @param cause 원인 예외
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
