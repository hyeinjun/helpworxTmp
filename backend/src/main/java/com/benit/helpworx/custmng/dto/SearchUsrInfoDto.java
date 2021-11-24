package com.benit.helpworx.custmng.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchUsrInfoDto {

    private String coCd;

    private String usrId;

    private String nm;

    private Integer page;

}
