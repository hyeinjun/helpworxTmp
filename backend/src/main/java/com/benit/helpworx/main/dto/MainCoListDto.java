package com.benit.helpworx.main.dto;

import com.benit.helpworx.main.resource.MainCoListResource;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MainCoListDto {
    private String coCd;

    private String coNm;

    private boolean useYn;

    private boolean pwxUseYn;

    private boolean swxUseYn;

    private boolean rplUseYn;

    private boolean rcoUseYn;

    //사용 시스템의 갯수를 센다.
    public int countSys(){
        int sysCnt=0;

        if(this.pwxUseYn){
            sysCnt++;
        }
        if(this.swxUseYn){
            sysCnt++;
        }
        if(this.rplUseYn){
            sysCnt++;
        }
        if(this.rcoUseYn){
            sysCnt++;
        }

        return sysCnt;
    }

    public MainCoListResource toListResource(Long custNum){
        return MainCoListResource.builder()
                .coCd(this.coCd)
                .coNm(this.coNm)
                .useYn(this.useYn)
                .sysNum(this.countSys())
                .custNum(custNum)
                .build();
    }
}
