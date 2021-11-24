package com.benit.helpworx.inqu.resource;

import lombok.*;

import java.util.Date;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogListResource {
    private int inquId;
    private String sysNm;
    private String dscr;
    private String tim;
    private Date date;
    private int uncnfrmCnt;
}
