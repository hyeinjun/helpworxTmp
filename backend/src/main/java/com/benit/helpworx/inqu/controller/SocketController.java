package com.benit.helpworx.inqu.controller;

import com.benit.helpworx.common.entity.InquLog;
import com.benit.helpworx.common.entity.RltmInqu;
import com.benit.helpworx.custmng.service.CustomerManagementService;
import com.benit.helpworx.inqu.dto.InquLogDto;
import com.benit.helpworx.inqu.dto.SocketDto;
import com.benit.helpworx.inqu.resource.InquLogResource;
import com.benit.helpworx.inqu.resource.InquSocketResource;
import com.benit.helpworx.inqu.resource.NewInquResource;
import com.benit.helpworx.inqu.service.InquService;
import com.benit.helpworx.inqu.service.OperatorInquService;
import com.benit.helpworx.inqu.service.OperatorInquServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class SocketController {
    @Autowired
    private InquService inquService;

    @Autowired
    private CustomerManagementService customerManagementService;

    @Autowired
    private OperatorInquServiceImpl operatorInquServiceImpl;


    // 메시지를 받을 endpoint를 receive로 설정
    @MessageMapping("/receive")

    // send로 메시지를 반환
    @SendTo("/topic/log")
    // receive에서 메시지를 받아서 send로 보냄

    /**
     * sender에게 dto의 형식으로 메시지를 받은 후
     * receiver가 메시지를 받을 형식을 리턴
     */
    public InquSocketResource SocketHandler(InquLogDto dto){
        // 생성자로 반환값 생성
        int inquLogId = inquService.findOneByInquIdAndInquDscrAndTim(
                dto.getInquId(), dto.getInquDscr(), dto.getTim());
        SimpleDateFormat sdf = new SimpleDateFormat("kk:mm");
        InquSocketResource ret = dto.toInquSocketResource();
        ret.setTim(sdf.format(dto.getTim()));
        ret.setInquLogId(inquLogId);
        String usrNm = customerManagementService.detailCustomer(dto.getSndr()).getNm();
        ret.setUsrNm(usrNm);

        // 답변자 찾아오기
        String answrr = inquService.findInquByInquId(dto.getInquId()).getAnswrr();
        ret.setAnswrr(answrr);
        return ret;
    }


    @MessageMapping("/alert")
    @SendTo("/topic/alert")
    public NewInquResource AlertHandler(InquLogDto dto){
        String sbjct = inquService.findInquByInquId(dto.getInquId()).getSbjct();
        NewInquResource ret = new NewInquResource(dto.getInquId(), sbjct, dto.getInquUsrId(), dto.getCoCd(),
                operatorInquServiceImpl.calcDate(dto.getTim()));
        return ret;
    }

}
