package com.benit.helpworx.comng.resource;

import com.benit.helpworx.comng.dto.CoDto;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoDetailResource {

    private String coCd;

    private String coNm;

    private boolean useYn;

    private String coDscr;

    private String contBeginDt;

    private String contEndDt;

    private boolean pwxUseYn;

    private boolean swxUseYn;

    private boolean rplUseYn;

    private boolean rcoUseYn;

    private String remnNm;

    private String bizmanNo;

    private String coAddr;

    private String fondDt;

    public CoDto toCoDto(){
        System.out.println(contBeginDt);
        System.out.println(contEndDt);

        return CoDto.builder()
                .coCd(this.coCd)
                .coNm(this.coNm)
                .coDscr(this.coDscr)
                .useYn(this.useYn)
                .contBeginDt(this.contBeginDt)
                .contEndDt(this.contEndDt)
                .pwxUseYn(this.pwxUseYn)
                .swxUseYn(this.swxUseYn)
                .rplUseYn(this.rplUseYn)
                .rcoUseYn(this.rcoUseYn)
                .remnNm(this.remnNm)
                .bizmanNo(this.bizmanNo)
                .coAddr(this.coAddr)
                .fondDt(this.fondDt)
                .build();

    }

}
