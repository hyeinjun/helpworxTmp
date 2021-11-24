package com.benit.helpworx.comng.resource;

import com.benit.helpworx.comng.dto.CoDto;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoListResource {

    private String coCd;

    private String coNm;

    private Long custNum;

    private int sysNum;

    private boolean useYn;

    private String contBeginDt;

    private String contEndDt;

    public CoDto toDto(){
        return CoDto.builder()
                .coCd(this.coCd)
                .coNm(this.coNm)
                .useYn(this.useYn)
                .contBeginDt(this.contBeginDt)
                .contEndDt(this.contEndDt)
                .build();
    }
}
