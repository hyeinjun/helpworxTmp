package com.benit.helpworx.sr.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SrProgressDto {
    private int srId;
    private String regId;
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

    private boolean returnState;
}
