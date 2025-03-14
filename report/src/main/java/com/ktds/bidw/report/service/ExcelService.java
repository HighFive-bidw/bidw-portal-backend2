package com.ktds.bidw.report.service;

import com.ktds.bidw.report.domain.ReportData;
import com.ktds.bidw.report.dto.ExcelResult;

/**
 * Excel 파일 생성 서비스 인터페이스입니다.
 */
public interface ExcelService {
    
    /**
     * 리포트 데이터를 기반으로 Excel 파일을 생성합니다.
     *
     * @param reportData 리포트 데이터
     * @param reportName 리포트 이름
     * @return Excel 생성 결과
     */
    ExcelResult generateExcel(ReportData reportData, String reportName);
}
