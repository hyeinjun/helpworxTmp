package com.benit.helpworx.infomod.dto;

import com.benit.helpworx.common.entity.Notfc;
import lombok.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotfcDto {

    private int trgetId;

    private String usrId;

    private String state;

    private String trgetUrl;

    private String func;

    private String notfcTim;

    public Notfc toEntity() throws ParseException {
        return Notfc.builder()
                .func(this.func)
                .state(this.state)
                .notfcTim(changeStrToDt(this.notfcTim))
                .usrId(this.usrId)
                .trgetUrl(this.trgetUrl)
                .trgetId(this.trgetId)
                .build();
    }

    public Date changeStrToDt(String str) throws ParseException {
        DateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.parse(str);
    }
}
