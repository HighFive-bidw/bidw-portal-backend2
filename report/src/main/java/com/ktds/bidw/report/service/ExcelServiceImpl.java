package com.ktds.bidw.report.service;

import com.ktds.bidw.report.domain.ReportData;
import com.ktds.bidw.report.dto.ExcelResult;
import com.ktds.bidw.report.exception.FileStorageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


/**
 * Excel 파일 생성 서비스 구현 클래스입니다.
 * Claim Check 패턴을 구현하고 관련 메트릭을 수집합니다.
 */
@Slf4j
@Service
public class ExcelServiceImpl implements ExcelService {

    private final FileStorage fileStorage;

    // 메트릭 추가
    private final Timer excelGenerationTimer;
    private final Counter excelFileSizeCounter;
    private final Timer excelFileStorageTimer;

    /**
     * 생성자를 통해 의존성을 주입받습니다.
     */
    @Autowired
    public ExcelServiceImpl(
            FileStorage fileStorage,
            @Qualifier("excelGenerationTimer") Timer excelGenerationTimer,
            @Qualifier("excelFileSizeCounter") Counter excelFileSizeCounter,
            @Qualifier("excelFileStorageTimer") Timer excelFileStorageTimer) {
        this.fileStorage = fileStorage;
        this.excelGenerationTimer = excelGenerationTimer;
        this.excelFileSizeCounter = excelFileSizeCounter;
        this.excelFileStorageTimer = excelFileStorageTimer;
    }

    /**
     * 리포트 데이터를 기반으로 Excel 파일을 생성합니다. (Claim Check 패턴 구현)
     */
    @Override
    public ExcelResult generateExcel(ReportData reportData, String reportName) {
        return excelGenerationTimer.record(() -> {
            try (Workbook workbook = new XSSFWorkbook()) {
                // 워크시트 생성
                Sheet sheet = workbook.createSheet(reportName);

                // 헤더 스타일 설정
                CellStyle headerStyle = workbook.createCellStyle();
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);
                headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
                headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                headerStyle.setBorderBottom(BorderStyle.THIN);
                headerStyle.setBorderLeft(BorderStyle.THIN);
                headerStyle.setBorderRight(BorderStyle.THIN);
                headerStyle.setBorderTop(BorderStyle.THIN);

                // 데이터 스타일 설정
                CellStyle dataStyle = workbook.createCellStyle();
                dataStyle.setBorderBottom(BorderStyle.THIN);
                dataStyle.setBorderLeft(BorderStyle.THIN);
                dataStyle.setBorderRight(BorderStyle.THIN);
                dataStyle.setBorderTop(BorderStyle.THIN);

                // 헤더 행 생성
                List<String> headers = reportData.getHeaders();
                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < headers.size(); i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(headers.get(i));
                    cell.setCellStyle(headerStyle);
                    // 열 너비 자동 조정을 위한 초기 설정
                    sheet.setColumnWidth(i, 256 * 15); // 15자 너비
                }

                // 데이터 행 생성
                List<Map<String, Object>> rows = reportData.getRows();
                for (int i = 0; i < rows.size(); i++) {
                    Row row = sheet.createRow(i + 1);
                    Map<String, Object> rowData = rows.get(i);

                    for (int j = 0; j < headers.size(); j++) {
                        Cell cell = row.createCell(j);
                        String header = headers.get(j);
                        Object value = rowData.get(header);

                        setCellValue(cell, value);
                        cell.setCellStyle(dataStyle);
                    }
                }

                // 리포트 메타데이터 시트 추가
                Sheet metaSheet = workbook.createSheet("메타데이터");
                Row titleRow = metaSheet.createRow(0);
                titleRow.createCell(0).setCellValue("리포트명");
                titleRow.createCell(1).setCellValue(reportName);

                Row dateRow = metaSheet.createRow(1);
                dateRow.createCell(0).setCellValue("생성일시");
                dateRow.createCell(1).setCellValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

                Row rowCountRow = metaSheet.createRow(2);
                rowCountRow.createCell(0).setCellValue("행 수");
                rowCountRow.createCell(1).setCellValue(rows.size());

                // 열 너비 자동 조정
                metaSheet.autoSizeColumn(0);
                metaSheet.autoSizeColumn(1);

                // 워크북을 바이트 배열로 변환
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                workbook.write(outputStream);
                byte[] excelBytes = outputStream.toByteArray();

                // 파일 크기 메트릭 증가
                excelFileSizeCounter.increment(excelBytes.length);
                log.info("Excel file generated with size: {} bytes", excelBytes.length);

                // 파일명 생성
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                String fileName = reportName.replaceAll("\\s+", "_") + "_" + timestamp + ".xlsx";

                // 파일 저장소에 저장하고 URL 반환 (Claim Check 패턴 적용)
                // 파일 저장 시간 측정을 위한 타이머 추가
                String fileUrl = excelFileStorageTimer.record(() -> {
                    try {
                        return fileStorage.storeFile(excelBytes, fileName);
                    } catch (Exception e) {
                        log.error("Error storing Excel file: {}", e.getMessage(), e);
                        throw e;
                    }
                });

                return new ExcelResult(fileUrl, fileName);
            } catch (IOException e) {
                log.error("Excel 파일 생성 중 오류가 발생했습니다", e);
                throw new FileStorageException("Excel 파일 생성에 실패했습니다: " + e.getMessage());
            }
        });
    }

    /**
     * 셀에 적절한 타입의 값을 설정합니다.
     */
    private void setCellValue(Cell cell, Object value) {
        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof LocalDateTime) {
            cell.setCellValue(((LocalDateTime) value).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        } else if (value instanceof LocalDate) {
            cell.setCellValue(((LocalDate) value).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        } else {
            cell.setCellValue(value.toString());
        }
    }
}
