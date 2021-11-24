package com.benit.helpworx.custmng.resource;

import com.benit.helpworx.custmng.dto.UsrAuthDto;
import com.benit.helpworx.custmng.dto.UsrDto;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsrResource {
    private String usrId;

    private String coCd;

    private String pwd;

    private String nm;

    private boolean useYn;

    private boolean userYn;

    private boolean operatorYn;

    private boolean managerYn;

    private String coNm;

    private String telNo;

    private String empno;

    private String email;

    public UsrDto toDto(){
        return UsrDto.builder()
                .coCd(this.coCd)
                .coNm(this.coNm)
                .nm(this.nm)
                .pwd(this.pwd)
                .useYn(this.useYn)
                .usrId(this.usrId)
                .email(this.email)
                .empno(this.empno)
                .telNo(this.telNo)
                .build();
    }

    public UsrAuthDto toCustAuthDto(){
        return UsrAuthDto.builder()
                .userYn(this.userYn)
                .operatorYn(this.operatorYn)
                .managerYn(this.managerYn)
                .coCd(this.coCd)
                .usrId(this.usrId)
                .build();
    }

    public void usrDtoToResource(UsrDto usrDto){
        this.usrId=usrDto.getUsrId();
        this.coCd=usrDto.getCoCd();
        this.coNm=usrDto.getCoNm();
        this.nm=usrDto.getNm();
        this.useYn=usrDto.getUseYn();
        this.pwd=usrDto.getPwd();
        this.email=usrDto.getEmail();
        this.empno=usrDto.getEmpno();
        this.telNo=usrDto.getTelNo();
    }

    public void authDtoToResource(UsrAuthDto usrAuthDto){
        this.managerYn=usrAuthDto.isManagerYn();
        this.operatorYn= usrAuthDto.isOperatorYn();
        this.userYn=usrAuthDto.isUserYn();
    }
}
