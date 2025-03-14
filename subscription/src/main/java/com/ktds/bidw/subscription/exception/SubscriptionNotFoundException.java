package com.ktds.bidw.subscription.exception;

import com.ktds.bidw.common.exception.ResourceNotFoundException;

/**
 * 구독 정보를 찾을 수 없을 때 발생하는 예외 클래스입니다.
 */
public class SubscriptionNotFoundException extends ResourceNotFoundException {
    
    public SubscriptionNotFoundException(String message) {
        super(message);
    }
}
