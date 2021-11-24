package com.benit.helpworx.sr.dto;

import com.benit.helpworx.common.entity.Sr;
import lombok.*;

import java.util.Date;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SrDto {

    private int srId;
    private int sysCd;
    private int jobGbId;
    private String usrId;
    private String reqGb;
    private String sbjct;
    private String dscr;
    private String state;
    private Date regTim;
    private String coCd;

    public Sr toEntity() {
        return Sr.builder()
                .srId(this.srId)
                .sysCd(this.sysCd)
                .coCd(this.coCd)
                .jobGbId(this.jobGbId)
                .usrId(this.usrId)
                .reqGb(this.reqGb)
                .sbjct(this.sbjct)
                .dscr(this.dscr)
                .state(this.state)
                .regTim(this.regTim)
                .build();
    }

}
