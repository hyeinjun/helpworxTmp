package com.benit.helpworx.infomod.resource;

import com.benit.helpworx.infomod.dto.InfoModReqStateDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfoModReqStateResource {

    private int reqId;

    private String state;

    public InfoModReqStateDto toDto(){
        return InfoModReqStateDto.builder()
                .reqId(this.reqId)
                .state(this.state)
                .build();
    }
}
