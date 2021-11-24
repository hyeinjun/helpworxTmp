package com.benit.helpworx.sr.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Builder
@Getter
@NoArgsConstructor
public class SrCntBySysAndReqDto {
    private String coCd;
    private long count;

    @QueryProjection
    public SrCntBySysAndReqDto(String coCd, long count) {
        this.coCd = coCd;
        this.count = count;
    }
}
