package com.benit.helpworx.infomod.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateInfoModReqDto {

    private int reqId;

    private String reqSbjct;

    private String reqDscr;

}
