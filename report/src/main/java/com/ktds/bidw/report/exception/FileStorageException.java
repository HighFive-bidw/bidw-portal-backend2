package com.ktds.bidw.report.exception;

import com.ktds.bidw.common.exception.BusinessException;

/**
 * 파일 저장소 관련 오류 발생 시 발생하는 예외 클래스입니다.
 */
public class FileStorageException extends BusinessException {
    
    public FileStorageException(String message) {
        super("FILE_STORAGE_ERROR", message);
    }
}
