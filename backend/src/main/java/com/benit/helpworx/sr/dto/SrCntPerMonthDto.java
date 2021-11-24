package com.benit.helpworx.sr.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 월 별 SR 건수 합
 */
@ToString
@Getter
public class SrCntPerMonthDto {

    private String month;
    private long count;

    @QueryProjection
    public SrCntPerMonthDto(String month, long count) {
        this.month = month;
        this.count = count;
    }
}
