package com.benit.helpworx.infomod.resource;

import com.benit.helpworx.infomod.dto.InfoModReqDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfoModReqResource {

    private int reqId;

    private String usrId;

    private String reqSbjct;

    private String reqDscr;

    private String state;

    private String reqTim;

    public InfoModReqDto toDto() {
        return InfoModReqDto.builder()
                .reqId(this.reqId)
                .usrId(this.usrId)
                .reqDscr(this.reqDscr)
                .reqSbjct(this.reqSbjct)
                .state(this.state)
                .reqTim(this.reqTim)
                .build();
    }

}
