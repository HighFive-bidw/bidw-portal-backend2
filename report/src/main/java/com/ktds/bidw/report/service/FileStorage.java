package com.ktds.bidw.report.service;

/**
 * 파일 저장소 서비스 인터페이스입니다.
 */
public interface FileStorage {
    
    /**
     * 파일을 저장합니다.
     *
     * @param data 파일 데이터
     * @param fileName 파일 이름
     * @return 저장된 파일의 접근 URL
     */
    String storeFile(byte[] data, String fileName);
    
    /**
     * 파일 URL을 생성합니다.
     *
     * @param fileName 파일 이름
     * @return 파일 접근 URL
     */
    String getFileUrl(String fileName);
}
