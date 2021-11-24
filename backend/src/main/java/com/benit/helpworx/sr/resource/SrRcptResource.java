package com.benit.helpworx.sr.resource;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SrRcptResource {
    private String recivrId;
    private String rcptDscr;
    private Date cmplPrdt;
}
