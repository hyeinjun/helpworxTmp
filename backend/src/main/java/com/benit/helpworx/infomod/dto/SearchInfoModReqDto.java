package com.benit.helpworx.infomod.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchInfoModReqDto {
    private String usrId;

    private String state;

    private Integer page;
}
