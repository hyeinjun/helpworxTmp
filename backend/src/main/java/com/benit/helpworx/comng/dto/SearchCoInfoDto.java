package com.benit.helpworx.comng.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchCoInfoDto {
    private String cocd;

    private String coNm;

    private Integer page;
}
