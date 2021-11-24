package com.benit.helpworx.custmng.dto;

import com.benit.helpworx.common.entity.Usr;
import com.benit.helpworx.custmng.resource.UsrResource;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsrDto {
    private String usrId;

    private String coCd;

    private String coNm;

    private String pwd;

    private String nm;

    private Boolean useYn;

    private String telNo;

    private String empno;

    private String email;


    public UsrResource toResource(){
        return UsrResource.builder()
                .coCd(this.coCd)
                .coNm(this.coNm)
                .usrId(this.usrId)
                .nm(this.nm)
                .useYn(this.useYn)
                .pwd(this.pwd)
                .email(this.email)
                .empno(this.empno)
                .telNo(this.telNo)
                .build();
    }

    public Usr toEntity(){
        return Usr.builder()
                .coCd(this.coCd)
                .coNm(this.coNm)
                .usrId(this.usrId)
                .nm(this.nm)
                .useYn(this.useYn)
                .pwd(this.pwd)
                .email(this.email)
                .empno(this.empno)
                .telNo(this.telNo)
                .build();
    }

}
