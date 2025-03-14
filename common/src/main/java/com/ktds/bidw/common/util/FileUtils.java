package com.ktds.bidw.common.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * 파일 처리 유틸리티 클래스입니다.
 */
public class FileUtils {
    
    /**
     * MultipartFile을 지정된 경로에 저장합니다.
     *
     * @param file 업로드된 파일
     * @param uploadDir 업로드 디렉토리
     * @param fileName 저장할 파일명
     * @return 저장된 파일 경로
     * @throws IOException 파일 저장 중 오류 발생 시
     */
    public static String saveFile(MultipartFile file, String uploadDir, String fileName) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        return filePath.toString();
    }
    
    /**
     * 파일을 삭제합니다.
     *
     * @param filePath 삭제할 파일 경로
     * @return 삭제 성공 여부
     */
    public static boolean deleteFile(String filePath) {
        try {
            File file = new File(filePath);
            return file.delete();
        } catch (Exception e) {
            return false;
        }
    }
}
