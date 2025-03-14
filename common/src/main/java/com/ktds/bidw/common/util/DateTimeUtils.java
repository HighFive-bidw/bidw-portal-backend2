package com.ktds.bidw.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 날짜/시간 변환 유틸리티 클래스입니다.
 */
public class DateTimeUtils {
    
    /**
     * Date를 LocalDateTime으로 변환합니다.
     *
     * @param date Date 객체
     * @return LocalDateTime 객체
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
    
    /**
     * LocalDateTime을 Date로 변환합니다.
     *
     * @param localDateTime LocalDateTime 객체
     * @return Date 객체
     */
    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    
    /**
     * LocalDateTime을 지정된 형식의 문자열로 변환합니다.
     *
     * @param localDateTime LocalDateTime 객체
     * @param pattern 날짜/시간 형식
     * @return 포맷팅된 문자열
     */
    public static String formatDateTime(LocalDateTime localDateTime, String pattern) {
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }
}
