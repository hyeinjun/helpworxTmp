package com.benit.helpworx.comng.dto;

import com.benit.helpworx.comng.resource.CoNameListResource;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoNmDto {

    private String coCd;

    private String coNm;

    private boolean useYn;

    public CoNameListResource toListResource(){

        return CoNameListResource.builder()
                .coCd(this.coCd)
                .coNm(this.coNm)
                .useYn(this.useYn)
                .build();
    }
}
