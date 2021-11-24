package com.benit.helpworx.login.dto;

import com.benit.helpworx.login.resource.UsrInfoResource;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsrInfoDto {

    private String usrId;

    private String coCd;

    private String coNm;

    private String nm;

    private boolean useYn;

    public void toResource(UsrInfoResource usrInfoResource){
        usrInfoResource.setUsrId(this.usrId);
        usrInfoResource.setNm(this.nm);
        usrInfoResource.setCoCd(this.coCd);
        usrInfoResource.setCoNm(this.coNm);

        return;
    }
}
