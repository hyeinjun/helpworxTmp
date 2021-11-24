package com.benit.helpworx.common.entity;

import com.benit.helpworx.comng.dto.CoDto;
import com.benit.helpworx.comng.dto.CoNmDto;
import com.benit.helpworx.main.dto.EndSchdCoDto;
import com.benit.helpworx.main.dto.MainCoListDto;
import com.benit.helpworx.main.dto.NewContCoDto;
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
@Table(name = "co")
public class Co {

    @Id
    @Column(name = "co_cd")
    @NotNull
    private String coCd;

    @Column(name = "co_nm")
    @NotNull
    private String coNm;

    @Column(name = "use_yn")
    @NotNull
    private boolean useYn;

    @Column(name = "co_dscr")
    private String coDscr;

    @Column(name = "cont_begin_dt")
    @NotNull
    private Date contBeginDt;

    @Column(name = "cont_end_dt")
    @NotNull
    private Date contEndDt;

    @Column(name = "pwx_use_yn")
    @NotNull
    private boolean pwxUseYn;

    @Column(name = "swx_use_yn")
    @NotNull
    private boolean swxUseYn;

    @Column(name = "rpl_use_yn")
    @NotNull
    private boolean rplUseYn;

    @Column(name = "rco_use_yn")
    @NotNull
    private boolean rcoUseYn;

    @Column(name="remn_nm")
    private String remnNm;

    @Column(name="bizman_no")
    private String bizmanNo;

    @Column(name="co_addr")
    private String coAddr;

    @Column(name="fond_dt")
    private Date fondDt;

    public CoDto toDto(Optional<Co> co) throws ParseException {
        return CoDto.builder()
                .coCd(co.get().coCd)
                .coNm(co.get().coNm)
                .coDscr(co.get().coDscr)
                .contBeginDt(changeDtToStr(co.get().contBeginDt))
                .contEndDt(changeDtToStr(co.get().contEndDt))
                .useYn(co.get().useYn)
                .pwxUseYn(co.get().pwxUseYn)
                .swxUseYn(co.get().swxUseYn)
                .rplUseYn(co.get().rplUseYn)
                .rcoUseYn(co.get().rcoUseYn)
                .remnNm(co.get().remnNm)
                .bizmanNo(co.get().bizmanNo)
                .coAddr(co.get().coAddr)
                .fondDt(changeDtToStr(co.get().fondDt))
                .build();
    }

    public CoNmDto toNmDto(){
        return CoNmDto.builder()
                .coCd(this.coCd)
                .coNm(this.coNm)
                .useYn(this.useYn)
                .build();
    }

    public MainCoListDto toMainListDto(){
        return MainCoListDto.builder()
                .coCd(this.coCd)
                .coNm(this.coNm)
                .useYn(this.useYn)
                .pwxUseYn(this.pwxUseYn)
                .swxUseYn(this.swxUseYn)
                .rcoUseYn(this.rcoUseYn)
                .rplUseYn(this.rplUseYn)
                .build();
    }

    public NewContCoDto toNewContDto() throws ParseException{
        return NewContCoDto.builder()
                .coCd(this.coCd)
                .coNm(this.coNm)
                .contBeginDt(changeDtToStr(this.contBeginDt))
                .build();
    }

    public EndSchdCoDto toEndSchdDto() throws ParseException{
        return EndSchdCoDto.builder()
                .coCd(this.coCd)
                .coNm(this.coNm)
                .contEndDt(changeDtToStr(this.contEndDt))
                .build();
    }

    public void update(CoDto coDto) throws ParseException{
        System.out.println("company update");
        this.coCd= coDto.getCoCd();
        this.coNm=coDto.getCoNm()==null? this.coNm : coDto.getCoNm();
        this.coDscr=coDto.getCoDscr();
        this.contBeginDt=coDto.getContBeginDt()==null? this.contBeginDt : changeStrToDt(coDto.getContBeginDt());
        this.contEndDt=coDto.getContEndDt()==null? this.contEndDt : changeStrToDt(coDto.getContEndDt());
        this.useYn=coDto.getUseYn()==null? this.useYn : coDto.getUseYn();
        this.pwxUseYn=coDto.getPwxUseYn()==null? this.pwxUseYn : coDto.getPwxUseYn();
        this.swxUseYn=coDto.getSwxUseYn()==null? this.swxUseYn : coDto.getSwxUseYn();
        this.rplUseYn=coDto.getRplUseYn()==null? this.rplUseYn : coDto.getRplUseYn();
        this.rcoUseYn=coDto.getRcoUseYn()==null? this.rcoUseYn : coDto.getRcoUseYn();
        this.remnNm=coDto.getRemnNm()==null? this.remnNm : coDto.getRemnNm();
        this.bizmanNo=coDto.getBizmanNo()==null? this.bizmanNo : coDto.getBizmanNo();
        this.coAddr= coDto.getCoAddr()==null? this.coAddr : coDto.getCoAddr();
        this.fondDt= coDto.getFondDt()==null? this.fondDt : changeStrToDt(coDto.getFondDt());
    }

    public String changeDtToStr(Date date){
        if(date!=null) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            return format.format(date);
        }
        return null;
    }

    public Date changeStrToDt(String str) throws ParseException {
        if(str!=null) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            return format.parse(str);
        }
        return null;
    }

}
