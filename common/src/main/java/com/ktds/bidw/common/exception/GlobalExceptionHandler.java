package com.ktds.bidw.common.exception;

import com.ktds.bidw.common.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 전역 예외 처리 클래스입니다.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * BusinessException 처리 핸들러입니다.
     *
     * @param e 발생한 BusinessException
     * @return 오류 응답
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {
        log.error("Business Exception: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(e.getMessage()));
    }
    
    /**
     * 인증 오류 예외 처리 핸들러입니다.
     *
     * @param e 발생한 예외
     * @return 오류 응답
     */
    @ExceptionHandler({InvalidCredentialsException.class})
    public ResponseEntity<ApiResponse<Void>> handleInvalidCredentialsException(Exception e) {
        log.error("Authentication Exception: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(e.getMessage()));
    }
    
    /**
     * 리소스 찾을 수 없음 예외 처리 핸들러입니다.
     *
     * @param e 발생한 예외
     * @return 오류 응답
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(ResourceNotFoundException e) {
        log.error("Resource Not Found: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(e.getMessage()));
    }
    
    /**
     * 유효성 검사 오류 처리 핸들러입니다.
     *
     * @param e 발생한 예외
     * @return 오류 응답
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String errorMessage = bindingResult.getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));
        
        log.error("Validation Error: {}", errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(errorMessage));
    }
    
    /**
     * 시스템 오류 처리 핸들러입니다.
     *
     * @param e 발생한 예외
     * @return 오류 응답
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleSystemException(Exception e) {
        log.error("System Exception: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("시스템 오류가 발생했습니다. 관리자에게 문의하세요."));
    }
}
