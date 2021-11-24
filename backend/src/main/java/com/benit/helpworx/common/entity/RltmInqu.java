package com.benit.helpworx.common.entity;

import com.benit.helpworx.inqu.dto.RltmInquDto;
import com.benit.helpworx.inqu.resource.NewInquResource;
import com.benit.helpworx.inqu.resource.SearchedRltmInquResource;
import com.benit.helpworx.inqu.service.OperatorInquService;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rltm_inqu")
public class RltmInqu {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "inqu_id")
    private int inquId;

    @Column(name = "sys_cd")
    private int sysCd;

    @Column(name = "usr_id")
    private String usrId;

    @Column(name = "state")
    private String state;

    @Column(name = "begin_tim")
    private Date beginTim;

    @Column(name = "end_tim")
    private Date endTim;

    @Column(name = "co_cd")
    private String coCd;

    @Column(name = "sbjct")
    private String sbjct;

    @Column(name = "answrr")
    private String answrr;


    public RltmInquDto toDto() {
        RltmInquDto dto = RltmInquDto.builder()
                .inquId(this.inquId)
                .sysCd(this.sysCd)
                .usrId(this.usrId)
                .state(this.state)
                .beginTim(this.beginTim)
                .endTim(this.endTim)
                .coCd(this.coCd)
                .sbjct(this.sbjct)
                .build();
        return dto;
    }

    public SearchedRltmInquResource toResource() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String end;
        if(this.endTim==null) end = "진행중";
        else end = sdf.format(this.endTim);
        SearchedRltmInquResource resource = SearchedRltmInquResource.builder()
                .inquId(this.inquId)
                .sysCd(this.sysCd)
                .usrId(this.usrId)
                .state(this.state)
                .beginTim(sdf.format(this.beginTim))
                .endTim(end)
                .coCd(this.coCd)
                .sbjct(this.sbjct)
                .build();
        return resource;
    }

    public NewInquResource toNewInquResource() {
        NewInquResource resource = NewInquResource.builder()
                .inquId(this.inquId)
                .sbjct(this.sbjct)
                .inquUsrId(this.usrId)
                .tim(calcDate(this.beginTim))
                .build();
        return resource;
    }
    private String calcDate(Date tim) {
        String ret = null;
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfTime = new SimpleDateFormat("a KK:mm");
        SimpleDateFormat sdfYear = new SimpleDateFormat("MM-dd");


        // 오늘 날짜
        Date date = new Date();

        // 어제 날짜
        Date bDate = new Date();
        bDate = new Date(bDate.getTime() - (1000*60*60*24-1));

        // 올해
        Calendar calDate = Calendar.getInstance();
        calDate.setTime(date);
        int thisYear = calDate.get(Calendar.YEAR);

        Calendar calTim = Calendar.getInstance();
        calTim.setTime(date);
        int timYear = calDate.get(Calendar.YEAR);

        if(sdfDate.format(date).equals(sdfDate.format(tim))) {
            ret = sdfTime.format(tim);
        } else if(sdfDate.format(bDate).equals(sdfDate.format(tim))) {
            ret = "어제";
        } else if(thisYear == timYear){
            ret = sdfYear.format(tim);
        } else {
            ret = sdfDate.format(tim);
        }

        return ret;
    }
}
