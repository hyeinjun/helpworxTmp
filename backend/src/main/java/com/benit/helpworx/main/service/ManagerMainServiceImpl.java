package com.benit.helpworx.main.service;

import com.benit.helpworx.common.entity.InfoModReq;
import com.benit.helpworx.common.repository.InfoModReqRepository;
import com.benit.helpworx.main.dto.EndSchdCoDto;
import com.benit.helpworx.main.dto.MainCoListDto;
import com.benit.helpworx.common.entity.Co;
import com.benit.helpworx.common.repository.CoRepository;
import com.benit.helpworx.main.dto.MainInfoModReqDto;
import com.benit.helpworx.main.dto.NewContCoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ManagerMainServiceImpl implements ManagerMainService {

    @Autowired
    private CoRepository coRepository;

    @Autowired
    private InfoModReqRepository infoModReqRepository;

    private static final int POST_COUNT=7;

    /**
     * 메인 카드에서 보여줄
     * 전체 회사 목록 조회
     *
     * @return
     */
    @Override
    @Transactional
    public List<MainCoListDto> mainListCompanyService() {
        Page<Co> pages=coRepository.findAll
                (PageRequest.of(0,POST_COUNT,
                        Sort.by(Sort.DEFAULT_DIRECTION,"coCd")));

        List<Co> cos=pages.getContent();
        List<MainCoListDto> mainCoDtos=new ArrayList<>();

        for(Co co: cos){
            mainCoDtos.add(co.toMainListDto());
        }

        return mainCoDtos;
    }

    /**
     * 메인에서 보여줄
     * 새로 계약한 회사 목록 조회
     *
     * @return
     */
    @Override
    public List<NewContCoDto> newContractCompanyService() throws ParseException {

        List<Co> cos=coRepository.findByBeginDt(PageRequest.of(0,POST_COUNT));

        List<NewContCoDto> newContCoDtos =new ArrayList<>();

        for(Co co: cos){
            newContCoDtos.add(co.toNewContDto());
        }

        return newContCoDtos;

    }

    /**
     * 메인에서 보여줄
     * 계약 종료일이 다가오는 회사 목록 조회
     *
     * @return
     */
    @Override
    public List<EndSchdCoDto> endScheduledCompanyService() throws ParseException {

        List<Co> cos=coRepository.findByEndDt(PageRequest.of(0,POST_COUNT));

        List<EndSchdCoDto> endSchdCoDtos =new ArrayList<>();

        for(Co co: cos){
            endSchdCoDtos.add(co.toEndSchdDto());
        }

        return endSchdCoDtos;
    }

    /**
     * 메인에서 보여줄
     * 완료되지 않은 요청 최근 순 정렬
     *
     * @return
     */
    @Override
    public List<MainInfoModReqDto> mainInfoReqListService() {

        List<InfoModReq> infoModReqs=infoModReqRepository.findByStateAndTim(PageRequest.of(0,POST_COUNT));

        List<MainInfoModReqDto> infoModReqDtos=new ArrayList<>();

        for(InfoModReq infoModReq:infoModReqs){
            infoModReqDtos.add(infoModReq.toMainDto());
        }

        return infoModReqDtos;
    }
}
