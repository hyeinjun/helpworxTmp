package com.benit.helpworx.common.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "sys")
public class Sys {
    @Id
    @Column(name = "sys_cd")
    private int sysCd;

    @Column(name = "sys_nm")
    private String sysNm;
}
