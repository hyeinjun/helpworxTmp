package com.benit.helpworx.main.dto;

import com.benit.helpworx.main.resource.EndSchdCoResource;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EndSchdCoDto {

    private String coCd;

    private String coNm;

    private String contEndDt;

    public EndSchdCoResource toCoResource(){
        return EndSchdCoResource.builder()
                .coCd(this.coCd)
                .coNm(this.coNm)
                .contEndDt(this.contEndDt)
                .build();
    }
}
