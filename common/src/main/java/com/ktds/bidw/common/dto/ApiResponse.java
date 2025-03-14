package com.ktds.bidw.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * API 응답 공통 DTO 클래스입니다.
 *
 * @param <T> 응답 데이터 타입
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    
    private String status;
    private String message;
    private T data;
    
    /**
     * 성공 응답을 생성합니다.
     *
     * @param data 응답 데이터
     * @param <T> 데이터 타입
     * @return API 응답 객체
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("success", null, data);
    }
    
    /**
     * 성공 응답을 생성합니다.
     *
     * @param message 메시지
     * @param data 응답 데이터
     * @param <T> 데이터 타입
     * @return API 응답 객체
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>("success", message, data);
    }
    
    /**
     * 오류 응답을 생성합니다.
     *
     * @param message 오류 메시지
     * @param <T> 데이터 타입
     * @return API 응답 객체
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>("error", message, null);
    }
}
