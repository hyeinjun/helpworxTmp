package com.benit.helpworx.login.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogInInfoDto {

    private String usrId;

    private String pwd;

    private boolean operatorOrManagerYN;
}
