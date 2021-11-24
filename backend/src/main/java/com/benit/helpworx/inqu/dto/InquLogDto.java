package com.benit.helpworx.inqu.dto;

import com.benit.helpworx.common.entity.InquLog;
import com.benit.helpworx.inqu.resource.InquLogResource;
import com.benit.helpworx.inqu.resource.InquSocketResource;
import lombok.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InquLogDto {
    private int inquLogId;
    private int inquId;
    private String inquUsrId;
    private String inquDscr;
    private Date tim;
    private String sndr;
    private String coCd;

    public InquLog toEntity() {
        return InquLog.builder()
                .inquLogId(this.inquLogId)
                .inquId(this.inquId)
                .inquUsrId(this.inquUsrId)
                .inquDscr(this.inquDscr)
                .tim(this.tim)
                .sndr(this.sndr)
                .build();
    }

    public InquLogResource toResource() {
        return InquLogResource.builder()
                .inquLogId(this.inquLogId)
                .inquId(this.inquId)
                .inquUsrId(this.inquUsrId)
                .inquDscr(this.inquDscr)
                .timD(this.tim)
                .sndr(this.sndr)
                .build();
    }

    // socket통신을 하기 위한 리소스로 변환
    public InquSocketResource toInquSocketResource() {
        return InquSocketResource.builder()
                .inquLogId(this.inquLogId)
                .inquId(this.inquId)
                .inquUsrId(this.inquUsrId)
                .inquDscr(this.inquDscr)
                .sndr(this.sndr)
                .build();
    }

}
