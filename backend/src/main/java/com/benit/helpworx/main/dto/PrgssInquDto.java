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
public class PrgssInquDto {
    private int inquId;
    private int sysCd;
    private String usrId;
    private Date beginTim;
    private String sbjct;

}
