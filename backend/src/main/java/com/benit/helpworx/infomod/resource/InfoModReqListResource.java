package com.benit.helpworx.infomod.resource;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfoModReqListResource {

    private int reqId;

    private String usrId;

    private String reqSbjct;

    private String state;

    private String reqTim;

}
