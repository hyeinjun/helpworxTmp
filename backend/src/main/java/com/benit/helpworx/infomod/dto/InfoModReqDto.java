package com.benit.helpworx.infomod.dto;

import com.benit.helpworx.common.entity.InfoModReq;
import com.benit.helpworx.infomod.resource.InfoModReqListResource;
import com.benit.helpworx.infomod.resource.InfoModReqResource;
import lombok.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfoModReqDto {

    private int reqId;

    private String usrId;

    private String reqSbjct;

    private String reqDscr;

    private String state;

    private String reqTim;

    public InfoModReq toEntity() throws ParseException {
        return InfoModReq.builder()
                .reqId(this.reqId)
                .usrId(this.usrId)
                .reqDscr(this.reqDscr)
                .reqSbjct(this.reqSbjct)
                .state(this.state)
                .reqTim(changeStrToDt(this.reqTim))
                .build();
    }

    public InfoModReqResource toResource(){
        return InfoModReqResource.builder()
                .reqId(this.reqId)
                .usrId(this.usrId)
                .reqSbjct(this.reqSbjct)
                .reqDscr(this.reqDscr)
                .state(this.state)
                .reqTim(this.reqTim)
                .build();
    }

    public InfoModReqListResource toListResource(){
        return InfoModReqListResource.builder()
                .reqId(this.reqId)
                .usrId(this.usrId)
                .reqSbjct(this.reqSbjct)
                .state(this.state)
                .reqTim(this.reqTim)
                .build();
    }

    public NotfcDto toNotfcDto(){
        return NotfcDto.builder()
                .trgetUrl("/customer/info/detail/"+this.reqId)
                .usrId(this.usrId)
                .trgetId(this.reqId)
                .func("정보수정요청")
                .state(this.state)
                .build();
    }

    public Date changeStrToDt(String str) throws ParseException {
        DateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.parse(str);
    }

}
