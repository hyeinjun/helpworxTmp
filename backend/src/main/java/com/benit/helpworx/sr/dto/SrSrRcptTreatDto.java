package com.benit.helpworx.sr.dto;

import com.benit.helpworx.common.entity.Sr;
import com.benit.helpworx.common.entity.SrRcptTreat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * Sr, SrRcptTreat Entity를 Join
 */
@ToString
@Getter
@Builder
@NoArgsConstructor
public class SrSrRcptTreatDto {

    private int srId;
    private String coCd;
    private int sysCd;
    private int jobGbId;
    private String usrId;
    private String reqGb;
    private String sbjct;
    private String dscr;
    private String state;
    private Date regTim;


    private int srRcptTreatId;
    private String recivrId;
    private String rcptDscr;
    private Date cmplPrdt;
    private String opetrId;
    private String treatDscr;
    private Date treatBeginTim;
    private Date treatEndTim;
    private String rqmtTim;

    @QueryProjection
    public SrSrRcptTreatDto(int srId, String coCd, int sysCd, int jobGbId, String usrId, String reqGb, String sbjct, String dscr, String state, Date regTim, int srRcptTreatId, String recivrId, String rcptDscr, Date cmplPrdt, String opetrId, String treatDscr, Date treatBeginTim, Date treatEndTim, String rqmtTim) {
        this.srId = srId;
        this.coCd = coCd;
        this.sysCd = sysCd;
        this.jobGbId = jobGbId;
        this.usrId = usrId;
        this.reqGb = reqGb;
        this.sbjct = sbjct;
        this.dscr = dscr;
        this.state = state;
        this.regTim = regTim;
        this.srRcptTreatId = srRcptTreatId;
        this.recivrId = recivrId;
        this.rcptDscr = rcptDscr;
        this.cmplPrdt = cmplPrdt;
        this.opetrId = opetrId;
        this.treatDscr = treatDscr;
        this.treatBeginTim = treatBeginTim;
        this.treatEndTim = treatEndTim;
        this.rqmtTim = rqmtTim;
    }

    public SrSrRcptTreatDto(Sr sr, SrRcptTreat srRcptTreat) {
        this.srId = sr.getSrId();
        this.coCd = sr.getCoCd();
        this.sysCd = sr.getSysCd();
        this.jobGbId = sr.getJobGbId();
        this.usrId = sr.getUsrId();
        this.reqGb = sr.getReqGb();
        this.sbjct = sr.getSbjct();
        this.dscr = sr.getDscr();
        this.state = sr.getState();
        this.regTim = sr.getRegTim();

        this.srRcptTreatId = srRcptTreat.getSrRcptTreatId();
        this.recivrId = srRcptTreat.getRecivrId();
        this.rcptDscr = srRcptTreat.getRcptDscr();
        this.cmplPrdt = srRcptTreat.getCmplPrdt();
        this.opetrId = srRcptTreat.getOpetrId();
        this.treatDscr = srRcptTreat.getTreatDscr();
        this.treatBeginTim = srRcptTreat.getTreatBeginTim();
        this.treatEndTim = srRcptTreat.getTreatEndTim();
        this.rqmtTim = srRcptTreat.getRqmtTim();
    }
}
