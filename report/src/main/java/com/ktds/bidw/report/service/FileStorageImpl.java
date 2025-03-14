package com.ktds.bidw.report.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.ktds.bidw.report.exception.FileStorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.OffsetDateTime;

/**
 * Azure Blob Storage를 이용한 파일 저장소 서비스 구현 클래스입니다.
 */
@Slf4j
@Service
public class FileStorageImpl implements FileStorage {

    private final BlobServiceClient blobServiceClient;
    private final BlobContainerClient containerClient;
    private final int expiryTimeInMinutes;
    
    /**
     * 생성자를 통해 Azure Storage 연결 설정을 초기화합니다.
     *
     * @param connectionString Azure Storage 연결 문자열
     * @param containerName 컨테이너 이름
     * @param expiryTimeInMinutes URL 만료 시간(분)
     */
    public FileStorageImpl(
            @Value("${azure.storage.connection-string}") String connectionString,
            @Value("${azure.storage.container.reports}") String containerName,
            @Value("${file.storage.expiry-time-minutes}") int expiryTimeInMinutes) {
        
        this.blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
        
        this.containerClient = blobServiceClient.getBlobContainerClient(containerName);
        
        // 컨테이너가 존재하지 않으면 생성
        if (!containerClient.exists()) {
            containerClient.create();
        }
        
        this.expiryTimeInMinutes = expiryTimeInMinutes;
    }
    
    /**
     * 파일을 Azure Blob Storage에 저장합니다.
     *
     * @param data 파일 데이터
     * @param fileName 파일 이름
     * @return 저장된 파일의 SAS URL
     */
    @Override
    public String storeFile(byte[] data, String fileName) {
        try {
            BlobClient blobClient = containerClient.getBlobClient(fileName);
            
            // 파일 업로드
            blobClient.upload(new ByteArrayInputStream(data), data.length, true);
            
            log.info("파일이 성공적으로 업로드되었습니다: {}", fileName);
            
            // SAS URL 반환
            return generateSignedUrl(fileName);
        } catch (Exception e) {
            log.error("파일 업로드 중 오류가 발생했습니다", e);
            throw new FileStorageException("파일 저장에 실패했습니다: " + e.getMessage());
        }
    }
    
    /**
     * 파일에 대한 SAS URL을 생성합니다.
     *
     * @param fileName 파일 이름
     * @return SAS URL
     */
    @Override
    public String getFileUrl(String fileName) {
        return generateSignedUrl(fileName);
    }
    
    /**
     * 파일에 대한 서명된 URL을 생성합니다.
     *
     * @param fileName 파일 이름
     * @return 서명된 URL
     */
    private String generateSignedUrl(String fileName) {
        BlobClient blobClient = containerClient.getBlobClient(fileName);
        
        // SAS 토큰 생성
        BlobSasPermission permission = new BlobSasPermission()
                .setReadPermission(true);
        
        OffsetDateTime expiryTime = OffsetDateTime.now().plusMinutes(expiryTimeInMinutes);
        
        BlobServiceSasSignatureValues values = new BlobServiceSasSignatureValues(expiryTime, permission);
        
        String sasToken = blobClient.generateSas(values);
        
        return blobClient.getBlobUrl() + "?" + sasToken;
    }
}
