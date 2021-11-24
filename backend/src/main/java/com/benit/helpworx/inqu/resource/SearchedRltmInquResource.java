package com.benit.helpworx.inqu.resource;

import lombok.*;

import javax.persistence.Column;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchedRltmInquResource {
    private int inquId;
    private String sysNm;
    private int sysCd;
    private String usrId;
    private String state;
    private String beginTim;
    private String endTim;
    private String coCd;
    private String sbjct;
}
