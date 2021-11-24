package com.benit.helpworx.inqu.dto;

import com.benit.helpworx.inqu.resource.InquLogResource;
import com.benit.helpworx.inqu.resource.LogListResource;
import lombok.*;

import java.util.Date;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogListDto {
    private int inquId;
    private String sysNm;
    private String dscr;
    private String tim;
    private Date date;

    public LogListResource toResource() {
        return LogListResource.builder()
                .inquId(this.inquId)
                .sysNm(this.sysNm)
                .dscr(this.dscr)
                .tim(this.tim)
                .date(this.date)
                .build();
    }
}
