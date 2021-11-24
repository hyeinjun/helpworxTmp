package com.benit.helpworx.main.resource;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MainInfoModReqResource {
    private int reqId;

    private String usrId;

    private String reqSbjct;

    private String state;
}
