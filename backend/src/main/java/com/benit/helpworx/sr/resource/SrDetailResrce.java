package com.benit.helpworx.sr.resource;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SrDetailResrce {
    private String coCd;
    private String sysNm;
    private String jobGb;
    private String reqGb;
    private String sbjct;
    private String dscr;
    private Date regTim;
    private String writer;
}
