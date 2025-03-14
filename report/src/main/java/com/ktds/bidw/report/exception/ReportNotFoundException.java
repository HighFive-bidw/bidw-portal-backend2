package com.ktds.bidw.report.exception;

import com.ktds.bidw.common.exception.ResourceNotFoundException;

/**
 * 리포트를 찾을 수 없을 때 발생하는 예외 클래스입니다.
 */
public class ReportNotFoundException extends ResourceNotFoundException {
    
    public ReportNotFoundException(String message) {
        super(message);
    }
}
