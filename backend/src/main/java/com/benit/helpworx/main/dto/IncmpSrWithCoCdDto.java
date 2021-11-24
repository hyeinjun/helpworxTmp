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
public class IncmpSrWithCoCdDto {
    private int srId;
    private String sysNm;
    private String sbjct;// 제목
    private String usrId; // 요청자
    private String regTim;//요청시간
    private String state;
}
