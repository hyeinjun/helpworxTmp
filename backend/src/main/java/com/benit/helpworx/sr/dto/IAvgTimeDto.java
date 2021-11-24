package com.benit.helpworx.sr.dto;

/**
 * SpringData Projection
 * 쿼리 결과에 대한 인터페이스
 * 운영자 차트에 들어가는 월별 평균 시간 계산 데이터
 */
public interface IAvgTimeDto {

    String getYearMonth();
    int getDay();
    int getHour();
    int getMinute();
}
