package com.ktds.bidw.auth.exception;

import com.ktds.bidw.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 사용자 인증 실패 시 발생하는 예외 클래스입니다.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidCredentialsException extends BusinessException {
    
    public InvalidCredentialsException(String message) {
        super("INVALID_CREDENTIALS", message);
    }
}
