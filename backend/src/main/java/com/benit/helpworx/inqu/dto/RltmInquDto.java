package com.benit.helpworx.inqu.dto;

import com.benit.helpworx.common.entity.RltmInqu;
import com.benit.helpworx.common.entity.Sr;
import lombok.*;

import javax.persistence.Column;
import java.util.Date;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RltmInquDto {
    private int inquId;
    private int sysCd;
    private String usrId;
    private String state;
    private Date beginTim;
    private Date endTim;
    private String coCd;
    private String sbjct;

    public RltmInqu toEntity() {
        return RltmInqu.builder()
                .inquId(this.inquId)
                .sysCd(this.sysCd)
                .usrId(this.usrId)
                .state(this.state)
                .beginTim(this.beginTim)
                .endTim(this.endTim)
                .coCd(this.coCd)
                .sbjct(this.sbjct)
                .build();
    }
}
