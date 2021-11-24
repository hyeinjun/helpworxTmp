package com.benit.helpworx.inqu.resource;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InquSocketResource {
    private int inquLogId;
    private int inquId;
    private String usrNm;
    private String inquUsrId;
    private String inquDscr;
    private String tim;
    private String sndr;
    private String answrr;
}
