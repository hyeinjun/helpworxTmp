package com.benit.helpworx.main.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class IncmpSrDto {
    private int srId;
    private String sysNm;
    private String sbjct;// 제목
    private String regTim;//요청시간
    private String state;
}
