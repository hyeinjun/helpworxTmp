package com.benit.helpworx.inqu.dto;

import com.benit.helpworx.common.entity.InquLog;
import com.benit.helpworx.inqu.resource.InquLogResource;
import com.benit.helpworx.inqu.resource.InquSocketResource;
import lombok.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InquListDto {
    private String usrId;
    private int sysCd;
    private String state;
    private String dscr;
    private int page;
}
