package com.benit.helpworx.login.resource;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsrInfoResource {
    private String usrId;

    private String coCd;

    private String coNm;

    private String nm;

    private boolean usrAuth;

    private boolean operatorAuth;

    private boolean managerAuth;

}
