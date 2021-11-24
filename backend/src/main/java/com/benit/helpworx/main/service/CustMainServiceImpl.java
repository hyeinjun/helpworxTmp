package com.benit.helpworx.main.service;

import com.benit.helpworx.common.entity.Co;
import com.benit.helpworx.common.entity.Sr;
import com.benit.helpworx.common.entity.Sys;
import com.benit.helpworx.common.repository.CoRepository;
import com.benit.helpworx.common.repository.SrRepository;
import com.benit.helpworx.common.repository.SysRepository;
import com.benit.helpworx.common.repository.UsrRepository;
import com.benit.helpworx.main.dto.AllAndMnCntWithSysCdDto;
import com.benit.helpworx.main.dto.IncmpSrDto;
import com.benit.helpworx.main.dto.IncmpSrWithCoCdDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustMainServiceImpl implements CustMainService{
    @Autowired
    UsrRepository usrRepository;
    @Autowired
    CoRepository coRepository;
    @Autowired
    SysRepository sysRepository;
    @Autowired
    SrRepository srRepository;

    /**
     * 회사에서 사용하는 시스템 리턴
     * @param coCd
     * @return
     */
    @Override
    public List<Sys> getSysNmByCoCd(String coCd) {
        List<String> ret = new ArrayList<>();
        Co co = coRepository.findById(coCd).orElse(null);
        if(co.isPwxUseYn()) ret.add("PeopleWorx");
        if(co.isSwxUseYn()) ret.add("SalesWorx");
        if(co.isRcoUseYn()) ret.add("r-CoCoAna");
        if(co.isRplUseYn()) ret.add("r-PLANNA");

        List<Sys> sysList = sysRepository.findAll();
        List<Sys> result = sysList
                .stream()
                .filter(s -> ret.contains(s.getSysNm()))
                .collect(Collectors.toList());
        System.out.println(result.toString());
        return result;
    }

    /**
     * sysCd, coCd별 전체 건수, 월별 건수 리턴
     * @param sysCd
     * @param coCd
     * @return
     */
    @Override
    public AllAndMnCntWithSysCdDto getAllAndMnCntWithSysCdAndCoCd(int sysCd, String coCd) {
        int totalCnt=srRepository.countByCoCdAndSysCd(coCd, sysCd);
        int dataModCnt=srRepository.countByCoCdAndSysCdAndReqGb(coCd, sysCd,"데이터수정");
        int imprvmCnt=srRepository.countByCoCdAndSysCdAndReqGb(coCd, sysCd,"개선");
        int errorCnt=srRepository.countByCoCdAndSysCdAndReqGb(coCd, sysCd,"오류");
        int inquCnt=srRepository.countByCoCdAndSysCdAndReqGb(coCd, sysCd,"문의");
        int thisMmCnt=getThisMmCnt(coCd, sysCd);
        int prevMmCnt=getPrevMmCnt(coCd, sysCd);

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
     * usrId의 미완료 SR 리턴
     * @param usrId
     * @return
     */
    @Override
    public List<IncmpSrDto> getIncmpListWithUsrId(String usrId) {
        List<IncmpSrDto> incmpSrDtoList = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // 10개정도 안읽은거 최근 순으로 정렬해서 list로 보내기
        List<Sr> srList = srRepository.findAllByUsrIdAndStateOrUsrIdAndStateOrderByRegTimDesc(usrId, "대기", usrId, "진행중");

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
     * coCd의 미완료 SR 리턴
     * @param coCd
     * @return
     */
    @Override
    public List<IncmpSrWithCoCdDto> getIncmpListWithCoCd(String coCd) {
        List<IncmpSrWithCoCdDto> incmpSrDtoList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // 10개정도 안읽은거 최근 순으로 정렬해서 list로 보내기
        List<Sr> srList = srRepository.findAllByCoCdAndStateOrCoCdAndStateOrderByRegTimDesc(coCd, "대기", coCd, "진행중");
        for(int idx=0; idx<srList.size(); idx++) {
            if(idx>10) break;
            String sysNm = sysRepository.findById(srList.get(idx).getSysCd()).get().getSysNm();

            String regTim = null;
            if(srList!=null && srList.get(idx).getRegTim()!=null) regTim = sdf.format(srList.get(idx).getRegTim());

            IncmpSrWithCoCdDto incmpSrDto = IncmpSrWithCoCdDto.builder()
                    .srId(srList.get(idx).getSrId())
                    .sysNm(sysNm)
                    .sbjct(srList.get(idx).getSbjct())
                    .usrId(srList.get(idx).getUsrId())
                    .regTim(regTim)
                    .state(srList.get(idx).getState())
                    .build();
            incmpSrDtoList.add(incmpSrDto);
        }

        return incmpSrDtoList;
    }

    /**
     * 전월 건수 리턴
     * @param coCd
     * @param sysCd
     * @return
     */
    private int getPrevMmCnt(String coCd, int sysCd) {
        // 전월 건수 구하기
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
        int prevMmCnt= srRepository.countByCoCdAndSysCdAndRegTimBetween(coCd, sysCd, prevMmStart, prevMmEnd);
        return prevMmCnt;
    }

    /**
     * 금월 건수 리턴
     * @param coCd
     * @param sysCd
     * @return
     */
    private int getThisMmCnt(String coCd, int sysCd) {
        // 금월 건수 구하기
        Calendar thisCalStart = Calendar.getInstance();
        Calendar thisCalEnd = Calendar.getInstance();

        int year = thisCalStart.get(Calendar.YEAR);
        int month = thisCalStart.get(Calendar.MONTH);
        thisCalStart.set(year, month,1,0,0,0);
        int date = thisCalStart.getActualMaximum(Calendar.DAY_OF_MONTH);

        thisCalEnd.set(year, month,date,23,59,59);

        Date thisMmStart = new Date(thisCalStart.getTimeInMillis());
        Date thisMmEnd = new Date(thisCalEnd.getTimeInMillis());

        int thisMmCnt= srRepository.countByCoCdAndSysCdAndRegTimBetween(coCd, sysCd, thisMmStart, thisMmEnd);
        return thisMmCnt;
    }
}
