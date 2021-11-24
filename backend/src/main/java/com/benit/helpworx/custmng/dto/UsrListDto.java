package com.benit.helpworx.custmng.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsrListDto {

    private List<UsrDto> usrDtos;

    private long totalCount;
}
