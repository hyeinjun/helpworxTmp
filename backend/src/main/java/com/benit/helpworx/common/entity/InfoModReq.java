package com.benit.helpworx.common.entity;

import com.benit.helpworx.infomod.dto.InfoModReqDto;
import com.benit.helpworx.infomod.dto.InfoModReqStateDto;
import com.benit.helpworx.infomod.dto.UpdateInfoModReqDto;
import com.benit.helpworx.main.dto.MainInfoModReqDto;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="info_mod_req")
public class InfoModReq {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="req_id")
    @NotNull
    private Integer reqId;

    @Column(name="usr_id")
    @NotNull
    private String usrId;

    @Column(name="req_sbjct")
    @NotNull
    private String reqSbjct;

    @Column(name="req_dscr")
    @NotNull
    private String reqDscr;

    @Column(name="state")
    @NotNull
    private String state;

    @Column(name="req_tim")
    @NotNull
    private Date reqTim;

    public InfoModReqDto toDto(){
        return InfoModReqDto.builder()
                .reqId(this.reqId)
                .usrId(this.usrId)
                .reqDscr(this.reqDscr)
                .reqSbjct(this.reqSbjct)
                .state(this.state)
                .reqTim(changeDtToStr(this.reqTim))
                .build();

    }
    public MainInfoModReqDto toMainDto(){

        return MainInfoModReqDto.builder()
                .reqId(this.reqId)
                .usrId(this.usrId)
                .reqSbjct(this.reqSbjct)
                .state(this.state)
                .build();
    }

    public String changeDtToStr(Date date){
        DateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    public Date changeStrToDt(String str) throws ParseException {
        DateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.parse(str);
    }

    public void update(UpdateInfoModReqDto updateInfoModReqDto){
        this.reqSbjct= updateInfoModReqDto.getReqSbjct()==null? this.reqSbjct: updateInfoModReqDto.getReqSbjct();
        this.reqDscr= updateInfoModReqDto.getReqDscr()==null? this.reqDscr: updateInfoModReqDto.getReqDscr();
    }

    public void updateState(InfoModReqStateDto infoModReqStateDto){
        this.state= infoModReqStateDto.getState()==null? this.state: infoModReqStateDto.getState();
    }

}
