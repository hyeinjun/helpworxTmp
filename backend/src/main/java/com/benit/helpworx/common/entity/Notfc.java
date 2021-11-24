package com.benit.helpworx.common.entity;

import com.benit.helpworx.notfc.dto.NotfcDto;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="notfc")
public class Notfc {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="notfc_id")
    @NotNull
    private Integer notfcId;

    @Column(name="usr_id")
    @NotNull
    private String usrId;

    @Column(name="state")
    @NotNull
    private String state;

    @Column(name="trget_url")
    @NotNull
    private String trgetUrl;

    @Column(name="notfc_tim")
    @NotNull
    private Date notfcTim;

    @Column(name="trget_id")
    @NotNull
    private Integer trgetId;

    @Column(name="func")
    @NotNull
    private String func;

    public NotfcDto toDto(String sbjct){
        return NotfcDto.builder()
                .notfcId(this.notfcId)
                .sbjct(sbjct)
                .func(this.func)
                .state(this.state)
                .trgetUrl(this.trgetUrl)
                .build();
    }

}
