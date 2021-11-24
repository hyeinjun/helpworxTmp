package com.benit.helpworx.common.entity;

import com.benit.helpworx.custmng.dto.UsrDto;
import com.benit.helpworx.login.dto.UsrInfoDto;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usr")
public class Usr {
    @Id
    @Column(name = "usr_id")
    @NotNull
    private String usrId;

    @Column(name="co_cd")
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private String coCd;

    @Column(name = "pwd")
    @NotNull
    private String pwd;

    @Column(name = "coNm")
    @NotNull
    private String coNm;

    @Column(name = "nm")
    @NotNull
    private String nm;

    @Column(name = "use_yn")
    @NotNull
    private boolean useYn;

    @Column(name = "tel_no")
    private String telNo;

    @Column(name = "empno")
    private String empno;

    @Column(name = "email")
    @NotNull
    private String email;

    public UsrDto toDto(Optional<Usr> usr){
        return UsrDto.builder()
                .usrId(usr.get().usrId)
                .coCd(usr.get().coCd)
                .coNm(usr.get().coNm)
                .nm(usr.get().nm)
                .email(usr.get().email)
                .empno(usr.get().empno)
                .telNo(usr.get().telNo)
                .useYn(usr.get().isUseYn())
                .build();
    }

    public UsrInfoDto toInfoDto(Optional<Usr> usr){
        return UsrInfoDto.builder()
                .usrId(usr.get().usrId)
                .coCd(usr.get().coCd)
                .coNm(usr.get().coNm)
                .nm(usr.get().nm)
                .useYn(usr.get().isUseYn())
                .build();
    }

    public void update(UsrDto usrDto){
        System.out.println("usr update");
        this.coCd=usrDto.getCoCd()==null? this.coCd : usrDto.getCoCd();
        this.coNm=usrDto.getCoNm()==null? this.coNm : usrDto.getCoNm();
        this.nm=usrDto.getNm()==null? this.nm : usrDto.getNm();
        this.pwd=usrDto.getPwd()==null? this.pwd : usrDto.getPwd();
        this.useYn=usrDto.getUseYn()==null? this.useYn : usrDto.getUseYn();
        this.empno=usrDto.getEmpno()==null? this.empno : usrDto.getEmpno();
        this.email=usrDto.getEmail()==null? this.email : usrDto.getEmail();
        this.telNo=usrDto.getTelNo()==null? this.telNo : usrDto.getTelNo();
    }
}
