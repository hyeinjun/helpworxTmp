package com.benit.helpworx.sr.dto;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SrRcptDto {
    private int srRcptTreatId;
    private int srId;
    private String recivrId;
    private String rcptDscr;
    private Date cmplPrdt;
    private Date treatBeginTim;
}
