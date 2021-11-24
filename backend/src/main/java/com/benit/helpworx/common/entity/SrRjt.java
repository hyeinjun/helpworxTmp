package com.benit.helpworx.common.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="sr_rjt")
public class SrRjt {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="sr_rjt_id")
    @NotNull
    private Integer srRjtId;

    @Column(name="sr_id")
    @NotNull
    private Integer srId;

    @Column(name="rjt_dscr")
    private String rjtDscr;

    @Column(name="rjt_tim")
    private Date rjtTim;

    @Column(name = "retrner")
    private String retrner;

}
