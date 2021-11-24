package com.benit.helpworx.main.resource;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewContCoResource {

    private String coCd;

    private String coNm;

    private String contBeginDt;
}
