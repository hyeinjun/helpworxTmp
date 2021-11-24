package com.benit.helpworx.infomod.resource;

import com.benit.helpworx.infomod.dto.UpdateInfoModReqDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateInfoModReqResource {

    private int reqId;

    private String reqSbjct;

    private String reqDscr;

    public UpdateInfoModReqDto toDto(){
        return UpdateInfoModReqDto.builder()
                .reqId(this.reqId)
                .reqSbjct(this.reqSbjct)
                .reqDscr(this.reqDscr)
                .build();
    }
}
