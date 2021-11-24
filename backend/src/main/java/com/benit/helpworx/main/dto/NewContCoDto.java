package com.benit.helpworx.main.dto;

import com.benit.helpworx.main.resource.NewContCoResource;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewContCoDto {

    private String coCd;

    private String coNm;

    private String contBeginDt;

    public NewContCoResource toCoResource(){
        return NewContCoResource.builder()
                .coCd(this.coCd)
                .coNm(this.coNm)
                .contBeginDt(this.contBeginDt)
                .build();
    }
}
