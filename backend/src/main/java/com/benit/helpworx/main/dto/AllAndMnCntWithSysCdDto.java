package com.benit.helpworx.main.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Setter
@Getter
public class AllAndMnCntWithSysCdDto {
    private int totalCnt;
    private int dataModCnt;
    private int imprvmCnt;
    private int errorCnt;
    private int inquCnt;

    private int thisMmCnt;
    private int prevMmCnt;

}
