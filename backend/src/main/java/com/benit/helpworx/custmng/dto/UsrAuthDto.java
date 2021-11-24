package com.benit.helpworx.custmng.dto;

import com.benit.helpworx.common.entity.Auth;
import com.benit.helpworx.custmng.resource.UsrResource;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsrAuthDto {

    private String usrId;

    private boolean userYn;

    private boolean managerYn;

    private boolean operatorYn;

    private String coCd;

    //유저 권한 반환
    public Auth toAuth(String auth){

        return Auth.builder()
                .auth(auth)
                .coCd(this.coCd)
                .usrId(this.usrId)
                .build();

    }

}
