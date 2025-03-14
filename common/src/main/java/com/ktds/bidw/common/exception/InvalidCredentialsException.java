package com.ktds.bidw.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 인증 실패 시 발생하는 예외 클래스입니다.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidCredentialsException extends BusinessException {
    
    /**
     * 오류 메시지를 받는 생성자입니다.
     *
     * @param message 오류 메시지
     */
    public InvalidCredentialsException(String message) {
        super(ErrorCode.INVALID_CREDENTIALS, message);
    }
}
