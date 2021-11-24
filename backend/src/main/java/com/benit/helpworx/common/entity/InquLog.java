package com.benit.helpworx.common.entity;

import com.benit.helpworx.inqu.dto.InquLogDto;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "inqu_log")
public class InquLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "inqu_log_id")
    private int inquLogId;

    @Column(name = "inqu_id")
    private int inquId;

    @Column(name = "inqu_usr_id")
    private String inquUsrId;

    @Column(name = "inqu_dscr")
    private String inquDscr;

    @Column(name = "tim")
    private Date tim;

    @Column(name = "sndr")
    private String sndr;

    @Column(name = "state")
    private boolean state;

    public InquLogDto toDto() {
        InquLogDto inquLogDto = InquLogDto.builder()
                .inquLogId(this.inquLogId)
                .inquId(this.inquId)
                .inquUsrId(this.inquUsrId)
                .inquDscr(this.inquDscr)
                .tim(this.tim)
                .sndr(this.sndr)
                .build();
        return inquLogDto;
    }
}
