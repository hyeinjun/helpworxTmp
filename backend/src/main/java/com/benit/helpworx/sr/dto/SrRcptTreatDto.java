package com.benit.helpworx.sr.dto;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SrRcptTreatDto {
    private int srRcptTreatId;
    private int srId;
    private String recivrId;
    private String rcptDscr;
    private Date cmplPrdt;
    private String opetrId;
    private String treatDscr;
    private Date treatBeginTim;
    private Date treatEndTim;
    private String rqmtTim;
    private String rcptTim;
}
