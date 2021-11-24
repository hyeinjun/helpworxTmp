package com.benit.helpworx.inqu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SocketDto {
    // 유저 이름 저장
    private String usrId;
    // 메시지 내용 저장
    private String msg;
}
