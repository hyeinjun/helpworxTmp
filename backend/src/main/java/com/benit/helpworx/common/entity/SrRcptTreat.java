package com.benit.helpworx.common.entity;

import com.benit.helpworx.sr.dto.SrDto;
import com.benit.helpworx.sr.dto.SrRcptTreatDto;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Optional;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "sr_rcpt_treat")
public class SrRcptTreat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "sr_rcpt_treat_id")
    private int srRcptTreatId;

//    @ManyToOne(targetEntity = Sr.class, fetch = FetchType.LAZY)
//    @JoinColumn(name = "sr_id")
//    @OnDelete(action = OnDeleteAction.CASCADE)
    @Column(name = "sr_id")
    private int srId;

    @Column(name = "recivr_id")
    private String recivrId;

    @Column(name = "rcpt_dscr")
    private String rcptDscr;

    @Column(name = "cmpl_prdt")
    private Date cmplPrdt;

    @Column(name = "opetr_id")
    private String opetrId;

    @Column(name = "treat_dscr")
    private String treatDscr;

    @Column(name = "treat_begin_tim")
    private Date treatBeginTim;

    @Column(name = "treat_end_tim")
    private Date treatEndTim;

    @Column(name = "rqmt_tim")
    private String rqmtTim;

    @Column(name = "rcpt_tim")
    private String rcptTim;

    public SrRcptTreatDto toDto() {
        SrRcptTreatDto srRcptTreatDto = SrRcptTreatDto.builder()
                .srRcptTreatId(this.srRcptTreatId)
                .srId(this.srId)
                .recivrId(this.recivrId)
                .rcptDscr(this.rcptDscr)
                .cmplPrdt(this.cmplPrdt)
                .opetrId(this.opetrId)
                .treatDscr(this.treatDscr)
                .treatBeginTim(this.treatBeginTim)
                .treatEndTim(this.treatEndTim)
                .rqmtTim(this.rqmtTim)
                .rcptTim(this.rcptTim)
                .build();
        return srRcptTreatDto;
    }

}
