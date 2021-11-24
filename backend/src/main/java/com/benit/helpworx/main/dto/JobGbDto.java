package com.benit.helpworx.main.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class JobGbDto {

    private int jobGbId;
    private int sysCd;
    private String job;
}
