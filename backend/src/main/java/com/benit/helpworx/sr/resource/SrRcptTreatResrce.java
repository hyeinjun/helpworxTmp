package com.benit.helpworx.sr.resource;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SrRcptTreatResrce {
    private int srId;
    private int jobGbId;
    private String usrId;
    private int sysCd;
    private String coCd;
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
    private String rcptTim;
}
