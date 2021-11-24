package com.benit.helpworx.main.dto;

import com.benit.helpworx.main.resource.MainInfoModReqResource;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MainInfoModReqDto {

    private int reqId;

    private String usrId;

    private String reqSbjct;

    private String state;

    public MainInfoModReqResource toResource(){
        return MainInfoModReqResource.builder()
                .reqId(this.reqId)
                .usrId(this.usrId)
                .reqSbjct(this.reqSbjct)
                .state(this.state)
                .build();
    }
}
