package com.benit.helpworx.sr.resource;

import lombok.*;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SrProgressResrce {
    private int srId;
    private String regState;
    private String regTim;

    private String recivrId;
    private String rcptState;
    private String rcptDate;

    private String opetrId;
    private String treatState;
    private String treatTim;

    private String doId;
    private String doState;
    private String doTim;
}
