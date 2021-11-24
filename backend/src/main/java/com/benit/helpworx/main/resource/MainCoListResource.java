package com.benit.helpworx.main.resource;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MainCoListResource {

    private String coCd;

    private String coNm;

    private Long custNum;

    private int sysNum;

    private boolean useYn;

}
