package com.ktds.bidw.report.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.ktds.bidw.report.exception.FileStorageException;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.OffsetDateTime;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Azure Blob Storage를 이용한 파일 저장소 서비스 구현 클래스입니다.
 */
@Slf4j
@Service
public class FileStorageImpl implements FileStorage {

    private final BlobServiceClient blobServiceClient;
    private final BlobContainerClient containerClient;
    private final int expiryTimeInMinutes;

    // 메트릭 추가
    private final Timer sasTokenGenerationTimer;
    private final Counter sasTokenIssuedCounter;
    private final Timer blobStorageOperationTimer;
    private final MeterRegistry meterRegistry;

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
            @Value("${file.storage.expiry-time-minutes}") int expiryTimeInMinutes,
            @Qualifier("sasTokenGenerationTimer") Timer sasTokenGenerationTimer,
            @Qualifier("sasTokenIssuedCounter") Counter sasTokenIssuedCounter,
            @Qualifier("blobStorageOperationTimer") Timer blobStorageOperationTimer,
            MeterRegistry meterRegistry) {
        
        this.blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
        
        this.containerClient = blobServiceClient.getBlobContainerClient(containerName);
        
        // 컨테이너가 존재하지 않으면 생성
        if (!containerClient.exists()) {
            containerClient.create();
        }

        this.expiryTimeInMinutes = expiryTimeInMinutes;
        this.sasTokenGenerationTimer = sasTokenGenerationTimer;
        this.sasTokenIssuedCounter = sasTokenIssuedCounter;
        this.blobStorageOperationTimer = blobStorageOperationTimer;
        this.meterRegistry = meterRegistry;
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
        // Timer.Sample을 사용하여 직접 시간 측정
        Timer.Sample sample = Timer.start(meterRegistry);
        try {
            BlobClient blobClient = containerClient.getBlobClient(fileName);
            blobClient.upload(new ByteArrayInputStream(data), data.length, true);
            log.info("파일이 성공적으로 업로드되었습니다: {}", fileName);
            return generateSignedUrl(fileName);
        } catch (Exception e) {
            log.error("파일 업로드 중 오류가 발생했습니다", e);
            throw new FileStorageException("파일 저장에 실패했습니다: " + e.getMessage());
        } finally {
            // 타이머 종료 및 메트릭 기록
            sample.stop(meterRegistry.timer("excel_file_storage_time_seconds"));
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
        return sasTokenGenerationTimer.record(() -> {
            BlobClient blobClient = containerClient.getBlobClient(fileName);

            // SAS 토큰 생성
            BlobSasPermission permission = new BlobSasPermission()
                    .setReadPermission(true);

            OffsetDateTime expiryTime = OffsetDateTime.now().plusMinutes(expiryTimeInMinutes);

            BlobServiceSasSignatureValues values = new BlobServiceSasSignatureValues(expiryTime, permission);

            String sasToken = blobClient.generateSas(values);

            // SAS 토큰 발급 카운터 증가
            sasTokenIssuedCounter.increment();

            return blobClient.getBlobUrl() + "?" + sasToken;
        });
    }
}
