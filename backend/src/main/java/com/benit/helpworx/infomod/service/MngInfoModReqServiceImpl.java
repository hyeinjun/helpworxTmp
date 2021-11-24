package com.benit.helpworx.infomod.service;

import com.benit.helpworx.common.entity.InfoModReq;
import com.benit.helpworx.common.repository.InfoModReqRepository;
import com.benit.helpworx.common.repository.NotfcRepository;
import com.benit.helpworx.infomod.dto.*;
import com.querydsl.core.QueryResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MngInfoModReqServiceImpl implements MngInfoModReqService{

    @Autowired
    private InfoModReqRepository infoModReqRepository;

    @Autowired
    private NotfcRepository notfcRepository;

    @Override
    @Transactional
    public void updateInfoModReq(InfoModReqStateDto infoModReqStateDto) {

        InfoModReq infoModReq = infoModReqRepository.findById(infoModReqStateDto.getReqId())
                .orElseThrow(()->new IllegalArgumentException("없는 요청 입니다. 요청 코드 = "+ infoModReqStateDto.getReqId()));

        infoModReq.updateState(infoModReqStateDto);
    }

    @Override
    @Transactional
    public void deleteInfoModReq(int reqId) {
        infoModReqRepository.deleteById(reqId);
    }

    @Override
    @Transactional
    public InfoModReqListDto listInfoModReq(SearchInfoModReqDto searchInfoModReqDto) {

        if(searchInfoModReqDto.getPage()==null){
            searchInfoModReqDto.setPage(new Integer(1));
        }

        QueryResults<InfoModReq> results=infoModReqRepository.findDynamicQueryMng(searchInfoModReqDto);
        List<InfoModReq> infoModReqs=results.getResults();

        List<InfoModReqDto> infoModReqDtos=new ArrayList<>();

        for(InfoModReq infoModReq:infoModReqs){
            infoModReqDtos.add(infoModReq.toDto());
        }

        return InfoModReqListDto.builder()
                .infoModReqDtos(infoModReqDtos)
                .totalCount(results.getTotal())
                .build();
    }

    @Override
    @Transactional
    public InfoModReqDto detailInfoModReq(int reqId) {

        InfoModReq infoModReq=infoModReqRepository.findById(reqId)
                .orElseThrow(()->new IllegalArgumentException("없는 요청 입니다. 요청 코드 = "+reqId));

        return infoModReq.toDto();
    }

    @Override
    @Transactional
    public void updateNotfc(NotfcDto notfcDto) throws ParseException {
        notfcRepository.save(notfcDto.toEntity());
    }
}
