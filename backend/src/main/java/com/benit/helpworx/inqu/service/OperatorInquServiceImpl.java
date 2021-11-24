package com.benit.helpworx.inqu.service;

import com.benit.helpworx.common.entity.InquLog;
import com.benit.helpworx.common.entity.RltmInqu;
import com.benit.helpworx.common.repository.InquLogRepository;
import com.benit.helpworx.common.repository.InquRepositorySupport;
import com.benit.helpworx.common.repository.RltmInquRepository;
import com.benit.helpworx.inqu.dto.LogListDto;
import com.benit.helpworx.inqu.dto.OperInquListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OperatorInquServiceImpl implements OperatorInquService{
    @Autowired
    InquRepositorySupport inquRepositorySupport;
    @Autowired
    RltmInquRepository rltmInquRepository;
    @Autowired
    InquLogRepository inquLogRepository;


    /**
     * coCd, sysCd, state, dscr을 가지고 inqu를 가지고 옴.
     * @param page
     * @param perPage
     * @param inquListDto
     * @return
     */
    @Override
    public List<RltmInqu> findInqus(int page, int perPage, OperInquListDto inquListDto) {

        List<RltmInqu> ret = inquRepositorySupport.findOperInqus(page, perPage, inquListDto);

        return ret;
    }

    /**
     * 조회된 목록 개수 리턴
     * @param inquListDto
     * @return
     */
    @Override
    public long getInquListCount(OperInquListDto inquListDto) {
        long count = inquRepositorySupport.getOperInquListCount(inquListDto);
        return count;
    }

    /**
     * 운영자가 답변한 미 종료 문의 목록 리턴
     * @param usrId
     */
    @Override
    public List<LogListDto> findInqusByAnswrr(String usrId) {
        List<RltmInqu> list = rltmInquRepository.findAllListByAnswrrAndState(usrId, "진행중");
        List<LogListDto> log = new ArrayList<>();
        for (int idx = 0; idx < list.size(); idx++) {
            int inquId = list.get(idx).getInquId();
            InquLog inquLog = inquLogRepository.findTop1ByInquIdOrderByTimDesc(inquId).orElse(null);
            if(inquLog!=null) {
                int sysCd = list.get(idx).getSysCd();
                String sysNm;
                if(sysCd == 1) sysNm = "PeopleWorx";
                else if(sysCd == 2) sysNm = "SalesWorx";
                else if(sysCd == 3) sysNm = "r-PLANNA";
                else sysNm = "r-CoCoAna";
                log.add(new LogListDto(inquId, sysNm, inquLog.getInquDscr(), calcDate(inquLog.getTim()), inquLog.getTim()));
            }
        }
        if(log.isEmpty()){
            log = null;
        } else {
            log.sort((o1, o2) ->  o2.getTim().compareTo(o1.getTim()));
        }

        return log;
    }

    /**
     * 아직 답변자가 없는 문의 목록 리턴
     * @return
     */
    @Override
    public List<RltmInqu> findnewInqus(String coCd) {
        List<RltmInqu> rltmInquList = rltmInquRepository.findAllByCoCdAndStateAndAnswrrOrderByBeginTimDesc(coCd, "진행중", null);
        return rltmInquList;
    }

    /**
     * 운영자가 답변 시작
     * @param usrId
     * @param inquId
     */
    @Override
    public void startAns(String usrId, int inquId) {
        RltmInqu rltmInqu = rltmInquRepository.findById(inquId).orElse(null);
        rltmInqu.setAnswrr(usrId);
        rltmInquRepository.save(rltmInqu);
    }

    @Override
    public int getNewInquCnt(String coCd) {
        int cnt = rltmInquRepository.countByCoCdAndAnswrr(coCd, null);
        return cnt;
    }

    /**
     * 해당 문의의 미확인 메시지 개수 리턴
     * @param inquId
     * @return
     */
    @Override
    public int cntUncnfrmLogByInquId(int inquId, String usrId) {
        int cnt = inquLogRepository.countByInquIdAndStateAndSndrNot(inquId,false, usrId);
        return cnt;
    }

    /**
     * 날짜 계산
     * 오늘 채팅: a KK:mm
     * 하루 전 채팅: 어제
     * 올해 채팅: MM-dd
     * 올해 이전 채팅: yyyy-MM-dd
     * @param tim
     * @return
     */
    public String calcDate(Date tim) {
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
