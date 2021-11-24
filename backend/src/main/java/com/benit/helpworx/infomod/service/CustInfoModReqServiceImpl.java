package com.benit.helpworx.infomod.service;

import com.benit.helpworx.common.entity.InfoModReq;
import com.benit.helpworx.common.repository.InfoModReqRepository;
import com.benit.helpworx.infomod.dto.InfoModReqDto;
import com.benit.helpworx.infomod.dto.InfoModReqListDto;
import com.benit.helpworx.infomod.dto.SearchInfoModReqDto;
import com.benit.helpworx.infomod.dto.UpdateInfoModReqDto;
import com.querydsl.core.QueryResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustInfoModReqServiceImpl implements CustInfoModReqService {

    @Autowired
    private InfoModReqRepository infoModReqRepository;

    @Override
    @Transactional
    public void insertInfoModReq(InfoModReqDto infoModReqDto) throws ParseException {
        System.out.println("insert infoModReq");
        InfoModReq infoModReq=infoModReqDto.toEntity();
        infoModReqRepository.save(infoModReq);
    }

    @Override
    @Transactional
    public void updateInfoModReq(UpdateInfoModReqDto updateInfoModReqDto) {

        InfoModReq infoModReq = infoModReqRepository.findById(updateInfoModReqDto.getReqId())
                .orElseThrow(()->new IllegalArgumentException("없는 요청 입니다. 요청 코드 = "+updateInfoModReqDto.getReqId()));

        infoModReq.update(updateInfoModReqDto);
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

        QueryResults<InfoModReq> results=infoModReqRepository.findDynamicQuery(searchInfoModReqDto);
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
}
