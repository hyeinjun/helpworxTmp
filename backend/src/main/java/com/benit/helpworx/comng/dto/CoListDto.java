package com.benit.helpworx.comng.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoListDto {

    private List<CoDto> coDtos;

    private Long totalCount;

}
