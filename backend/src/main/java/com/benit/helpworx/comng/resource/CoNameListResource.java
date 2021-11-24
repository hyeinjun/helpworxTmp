package com.benit.helpworx.comng.resource;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoNameListResource {

    private String coCd;

    private String coNm;

    private boolean useYn;
}
