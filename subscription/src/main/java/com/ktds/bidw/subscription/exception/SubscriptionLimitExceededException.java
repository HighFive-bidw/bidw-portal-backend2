package com.ktds.bidw.subscription.exception;

import com.ktds.bidw.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 구독 수 제한 초과 시 발생하는 예외 클래스입니다.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SubscriptionLimitExceededException extends BusinessException {
    
    public SubscriptionLimitExceededException(String message) {
        super("SUBSCRIPTION_LIMIT_EXCEEDED", message);
    }
}
