package com.benit.helpworx.inqu.dto;

import lombok.*;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperInquListDto {
    private String coCd;
    private int sysCd;
    private String state;
    private String dscr;
    private int page;
}
