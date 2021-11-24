package com.benit.helpworx.infomod.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InfoModReqListDto {

    private List<InfoModReqDto> infoModReqDtos;

    private Long totalCount;
}
