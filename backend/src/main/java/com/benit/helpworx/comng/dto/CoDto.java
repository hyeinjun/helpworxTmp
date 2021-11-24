package com.benit.helpworx.comng.dto;

import com.benit.helpworx.common.entity.Co;
import com.benit.helpworx.comng.resource.CoDetailResource;
import com.benit.helpworx.comng.resource.CoListResource;
import lombok.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoDto {
    private String coCd;

    private String coNm;

    private Boolean useYn;

    private String coDscr;

    private String contBeginDt;

    private String contEndDt;

    private Boolean pwxUseYn;

    private Boolean swxUseYn;

    private Boolean rplUseYn;

    private Boolean rcoUseYn;

    private String remnNm;

    private String bizmanNo;

    private String coAddr;

    private String fondDt;

    public CoListResource toListResource(Long custNum){

        return CoListResource.builder()
                .coCd(this.coCd)
                .coNm(this.coNm)
                .useYn(this.useYn)
                .sysNum(this.countSys())
                .contBeginDt(this.contBeginDt)
                .contEndDt(this.contEndDt)
                .custNum(custNum)
                .build();
    }

    public CoDetailResource toDetailResource(){

        return CoDetailResource.builder()
                .coCd(this.coCd)
                .coNm(this.coNm)
                .coDscr(this.coDscr)
                .contBeginDt(this.contBeginDt)
                .contEndDt(this.contEndDt)
                .useYn(this.useYn)
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

    public Co toEntity() throws ParseException{
        return Co.builder()
                .coCd(this.coCd)
                .coNm(this.coNm)
                .coDscr(this.coDscr)
                .contBeginDt(this.changeStrToDt(this.contBeginDt))
                .contEndDt(this.changeStrToDt(this.contEndDt))
                .useYn(this.useYn)
                .pwxUseYn(this.pwxUseYn)
                .swxUseYn(this.swxUseYn)
                .rplUseYn(this.rplUseYn)
                .rcoUseYn(this.rcoUseYn)
                .remnNm(this.remnNm)
                .bizmanNo(this.bizmanNo)
                .coAddr(this.coAddr)
                .fondDt(changeStrToDt(this.fondDt))
                .build();
    }

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

    public Date changeStrToDt(String str) throws ParseException {
        if(str!=null) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            System.out.println(str);
            return format.parse(str);
        }
        return null;
    }
}
