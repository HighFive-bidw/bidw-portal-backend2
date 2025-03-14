package com.ktds.bidw.report.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Excel 생성 결과 DTO 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExcelResult {
    
    private String fileUrl;
    private String fileName;
}
