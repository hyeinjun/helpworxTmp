package com.benit.helpworx.login.resource;

import com.benit.helpworx.login.dto.LogInInfoDto;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogInInfoResource {
    private String usrId;

    private String pwd;

    private boolean operatorOrManagerYn;

    private String refreshToken;

    public LogInInfoDto toDto(){
        return LogInInfoDto.builder()
                .usrId(this.usrId)
                .pwd(this.pwd)
                .operatorOrManagerYN(this.operatorOrManagerYn)
                .build();
    }
}
