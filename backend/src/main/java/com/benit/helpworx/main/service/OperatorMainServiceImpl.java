package com.benit.helpworx.main.service;

import com.benit.helpworx.common.entity.JobGb;
import com.benit.helpworx.common.entity.Sys;
import com.benit.helpworx.common.repository.JobGbRepository;
import com.benit.helpworx.common.repository.SysRepository;
import com.benit.helpworx.main.dto.*;

import com.benit.helpworx.common.repository.RltmInquRepository;
import com.benit.helpworx.common.entity.RltmInqu;
import com.benit.helpworx.common.entity.Sr;
import com.benit.helpworx.common.repository.SrRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class OperatorMainServiceImpl implements OperatorMainService{
    @Autowired
    SrRepository srRepository;

    @Autowired
    RltmInquRepository rltmInquRepository;

    @Autowired
    SysRepository sysRepository;

    @Autowired
    JobGbRepository jobGbRepository;

    /**
     * 시스템 별 전체 건수, 월별 건수 리턴
     * @param sysCd
     * @return
     */
    @Override
    public AllAndMnCntWithSysCdDto getAllAndMnCntWithSysCd(String coCd, int sysCd) {
        int totalCnt=srRepository.countByCoCdAndSysCd(coCd, sysCd);
        int dataModCnt=srRepository.countBySysCdAndReqGb(sysCd,"데이터수정");
        int imprvmCnt=srRepository.countBySysCdAndReqGb(sysCd,"개선");
        int errorCnt=srRepository.countBySysCdAndReqGb(sysCd,"오류");
        int inquCnt=srRepository.countBySysCdAndReqGb(sysCd,"문의");
        int thisMmCnt=getThisMmCnt(sysCd);
        int prevMmCnt=getPrevMmCnt(sysCd);

        AllAndMnCntWithSysCdDto ret = AllAndMnCntWithSysCdDto.builder()
                .totalCnt(totalCnt)
                .dataModCnt(dataModCnt)
                .imprvmCnt(imprvmCnt)
                .errorCnt(errorCnt)
                .inquCnt(inquCnt)
                .thisMmCnt(thisMmCnt)
                .prevMmCnt(prevMmCnt)
                .build();

        return ret;
    }

    /**
     * 회사의 미완료 SR 리턴
     * @return
     */
    @Override
    public List<IncmpSrDto> getIncmpList(String coCd) {
        List<IncmpSrDto> incmpSrDtoList = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // 10개정도 안읽은거 최근 순으로 정렬해서 list로 보내기
        List<Sr> srList = srRepository.findAllByCoCdAndStateOrCoCdAndStateOrderByRegTimDesc(coCd, "대기", coCd, "진행중");
        for(int idx=0; idx<srList.size(); idx++) {
            if(idx>10) break;
            String sysNm = sysRepository.findById(srList.get(idx).getSysCd()).get().getSysNm();
            IncmpSrDto incmpSrDto = IncmpSrDto.builder()
                    .srId(srList.get(idx).getSrId())
                    .sysNm(sysNm)
                    .sbjct(srList.get(idx).getSbjct())
                    .regTim(sdf.format(srList.get(idx).getRegTim()))
                    .state(srList.get(idx).getState())
                    .build();
            incmpSrDtoList.add(incmpSrDto);
        }

        return incmpSrDtoList;
    }

    /**
     * 회사의 진행중인 문의 리턴
     * @return
     */
    @Override
    public List<PrgssInquDto> getPrgssInquList(String coCd) {
        List<PrgssInquDto> prgssInquDtoList = new ArrayList<>();

        List<RltmInqu> rltmInquList = rltmInquRepository.findTop7ByCoCdAndStateOrderByBeginTimDesc(coCd, "진행중");

        for(int idx=0; idx<rltmInquList.size(); idx++) {
            if(idx>10) break;
            PrgssInquDto prgssInquDto = PrgssInquDto.builder()
                    .inquId(rltmInquList.get(idx).getInquId()   )
                    .sysCd(rltmInquList.get(idx).getSysCd())
                    .usrId(rltmInquList.get(idx).getUsrId())
                    .beginTim(rltmInquList.get(idx).getBeginTim())
                    .sbjct(rltmInquList.get(idx).getSbjct())
                    .build();
            prgssInquDtoList.add(prgssInquDto);
        }

        return prgssInquDtoList;
    }

    /**
     * 전체 시스템 목록 조회
     * @return
     */
    @Override
    public List<SysDto> getAllSys() {
        List<Sys> list = sysRepository.findAll();
        List<SysDto> result = new ArrayList<>();
        list.forEach(e -> {
            result.add(SysDto.builder()
                    .sysCd(e.getSysCd())
                    .sysNm(e.getSysNm())
                    .build());
        });
        return result;
    }

    /**
     * 전체 jobGB (업무구분) 목록 조회
     * @return
     */
    @Override
    public List<JobGbDto> getAllJobGb() {
        List<JobGb> list = jobGbRepository.findAll();
        List<JobGbDto> result = new ArrayList<>();
        list.forEach(e -> {
            result.add(JobGbDto.builder()
                    .jobGbId(e.getJobGbId())
                    .sysCd(e.getSysCd())
                    .job(e.getJob())
                    .build());
        });
        return result;
    }

    /**
     * 전월 건수 구하기
     * @param sysCd
     * @return
     */
    private int getPrevMmCnt(int sysCd) {
        Calendar prevCalStart = Calendar.getInstance();
        Calendar prevCalEnd = Calendar.getInstance();

        int prevYear = Calendar.getInstance().get(Calendar.YEAR);
        int prevMonth = Calendar.getInstance().get(Calendar.MONTH) - 1;
        if(prevMonth<0) {
            prevMonth=11;
            prevYear-=1;
        }

        prevCalStart.set(prevYear,prevMonth,1,0,0,0);
        int prevDate = prevCalStart.getActualMaximum(Calendar.DAY_OF_MONTH);
        prevCalEnd.set(prevYear, prevMonth, prevDate,23,59,59);

        Date prevMmStart = new Date(prevCalStart.getTimeInMillis());
        Date prevMmEnd = new Date(prevCalEnd.getTimeInMillis());
        int prevMmCnt= srRepository.countBySysCdAndRegTimBetween(sysCd, prevMmStart, prevMmEnd);
        return prevMmCnt;
    }

    /**
     * 금월 건수 구하기
     * @param sysCd
     * @return
     */
    private int getThisMmCnt(int sysCd) {
        Calendar thisCalStart = Calendar.getInstance();
        Calendar thisCalEnd = Calendar.getInstance();

        int year = thisCalStart.get(Calendar.YEAR);
        int month = thisCalStart.get(Calendar.MONTH);
        thisCalStart.set(year, month,1,0,0,0);
        int date = thisCalStart.getActualMaximum(Calendar.DAY_OF_MONTH);

        thisCalEnd.set(year, month,date,23,59,59);

        Date thisMmStart = new Date(thisCalStart.getTimeInMillis());
        Date thisMmEnd = new Date(thisCalEnd.getTimeInMillis());

        int thisMmCnt= srRepository.countBySysCdAndRegTimBetween(sysCd, thisMmStart, thisMmEnd);
        return thisMmCnt;
    }
}
