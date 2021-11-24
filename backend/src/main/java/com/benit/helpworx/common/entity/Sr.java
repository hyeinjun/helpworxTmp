package com.benit.helpworx.common.entity;

import com.benit.helpworx.sr.dto.SrDto;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sr", schema = "public", catalog = "postgres")
@Entity
public class Sr {
    @Id
    @Column(name = "sr_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int srId;

//    @ManyToOne(targetEntity = Usr.class, fetch = FetchType.LAZY)
//    @JoinColumn(name = "usr_id")
//    @OnDelete(action = OnDeleteAction.CASCADE)
    @Column(name = "usr_id")
    private String usrId;

//    @ManyToOne(targetEntity = JobGb.class, fetch = FetchType.LAZY)
//    @JoinColumn(name = "job_gb_id")
//    @OnDelete(action = OnDeleteAction.CASCADE)
    @Column(name = "job_gb_id")
    private int jobGbId;

    @Column(name = "sys_cd")
    private int sysCd;

    @Column(name = "req_gb")
    private String reqGb;

    @Column(name = "sbjct")
    private String sbjct;

    @Column(name = "dscr")
    private String dscr;

    @Column(name = "state")
    private String state;

    @Column(name = "reg_tim")
    private Date regTim;

    @Column(name = "co_cd")
    private String coCd;


    public SrDto toDto() {
        SrDto srDto = SrDto.builder()
                .srId(this.srId)
                .coCd(this.coCd)
                .sysCd(this.sysCd)
                .jobGbId(this.jobGbId)
                .usrId(this.usrId)
                .reqGb(this.reqGb)
                .sbjct(this.sbjct)
                .dscr(this.dscr)
                .state(this.state)
                .regTim(this.regTim)
                .build();
        return srDto;
    }

    /**
     * SR 수정
     * @param sysCd
     * @param jobGbId
     * @param reqGb
     * @param sbjct
     * @param dscr
     * @param regTim
     */
    public void update(int sysCd, int jobGbId, String reqGb, String sbjct, String dscr, Date regTim) {
        this.sysCd = sysCd;
        this.jobGbId = jobGbId;
        this.reqGb = reqGb;
        this.sbjct = sbjct;
        this.dscr = dscr;
        this.regTim = regTim;
    }

}
