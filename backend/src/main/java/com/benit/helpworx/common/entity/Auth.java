package com.benit.helpworx.common.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="auth")
public class Auth {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="auth_id")
    @NotNull
    private Integer authId;

    @Column(name="usr_id")
    @NotNull
    private String usrId;

    @Column(name="auth")
    @NotNull
    private String auth;

    @Column(name="co_cd")
    @NotNull
    private String coCd;

}
