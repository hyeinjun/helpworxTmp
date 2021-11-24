package com.benit.helpworx.sr.dto;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SrTreatDto {
    private int srRcptTreatId;
    private int srId;
    private String opetrId;
    private String treatDscr;
    private Date treatEndTim;
    private String rqmtTim;
}
