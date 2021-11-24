package com.benit.helpworx.common.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "job_gb")
public class JobGb {
    @Id
    @Column(name = "job_gb_id")
    private int jobGbId;

//    @ManyToOne(targetEntity = Sys.class, fetch = FetchType.LAZY)
//    @JoinColumn(name = "sys_cd")
//    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @Column(name = "sys_cd")
    private int sysCd;

    @Column(name = "job")
    private String job;

}
