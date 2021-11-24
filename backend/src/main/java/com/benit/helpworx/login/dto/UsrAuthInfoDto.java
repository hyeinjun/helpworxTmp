package com.benit.helpworx.login.dto;

import com.benit.helpworx.login.resource.UsrInfoResource;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsrAuthInfoDto {

    private boolean usrAuth;

    private boolean operatorAuth;

    private boolean managerAuth;

    public void toResource(UsrInfoResource usrInfoResource){
        usrInfoResource.setUsrAuth(this.usrAuth);
        usrInfoResource.setManagerAuth(this.managerAuth);
        usrInfoResource.setOperatorAuth(this.operatorAuth);

        return;
    }
}
