package com.benit.helpworx.inqu.resource;

import com.benit.helpworx.common.entity.InquLog;
import com.benit.helpworx.inqu.dto.InquLogDto;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InquLogResource {
    private int inquLogId;
    private int inquId;
    private String usrNm;
    private String inquUsrId;
    private String inquDscr;
    private String tim; // kk:mm
    private Date timD; // 기존 date
    private String sndr;

}
