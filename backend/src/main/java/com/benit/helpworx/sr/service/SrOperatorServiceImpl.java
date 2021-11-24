package com.benit.helpworx.sr.service;

import com.benit.helpworx.common.entity.Notfc;
import com.benit.helpworx.common.entity.Sr;
import com.benit.helpworx.common.entity.SrRcptTreat;
import com.benit.helpworx.common.entity.SrRjt;
import com.benit.helpworx.common.repository.*;
import com.benit.helpworx.sr.dto.*;
import com.benit.helpworx.sr.resource.RqmtTimeLineChartResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class SrOperatorServiceImpl implements SrOperatorService {
    @Autowired
    private SrRepository srRepository;

    @Autowired
    private SrRepositorySupport srRepositorySupport;

    @Autowired
    private SrRcptTreatRepository srRcptTreatRepository;

    @Autowired
    private SrRjtRepository srRjtRepository;

    @Autowired
    private NotfcRepository notfcRepository;

    /**
     * srId로 sr 리턴
     * @param srId
     * @return
     */
    @Override
    public SrDto getSrWithSrId(int srId) {
        SrDto srDto = null;
        Sr sr = srRepository.findBySrId(srId).orElse(null);
        if(Objects.nonNull(sr)) {
            srDto = sr.toDto();
        }

        return srDto;
    }

    /**
     * srId로 srRcptTreat 리턴
     * @param srId
     * @return
     */
    @Override
    public SrRcptTreatDto getSrRcptTreatWithSrId(int srId) {
        SrRcptTreatDto ret = null;
        SrRcptTreat srRcptTreat = srRcptTreatRepository.findBySrId(srId).orElse(null);
        if(Objects.nonNull(srRcptTreat)) {
            ret = srRcptTreat.toDto();
        }

        return ret;
    }

    /**
     * srRcpt 수정 / 저장
     * @param srRcptDto
     */
    @Override
    public void saveSrRcpt(SrRcptDto srRcptDto) {
        // 접수 내용 저장 시 진행중으로 SR state 수정
        Sr sr = srRepository.getOneBySrId(srRcptDto.getSrId());

        if (isSrRcptTreat(sr.getSrId()))
            srRcptTreatRepository.deleteBySrId(sr.getSrId());
        else {
            sr.setState("진행중");
            srRepository.save(sr);
        }

        long diffSec = (srRcptDto.getTreatBeginTim().getTime() - sr.getRegTim().getTime()) / 1000;
        long diffDate = diffSec / (60*60*24);
        long diffHour = (diffSec - diffDate * 60 * 60 * 24) / (60 * 60);
        long diffMin = (diffSec - diffDate * 60 * 60 * 24 - diffHour * 3600 ) / 60;
        String rcptTim = "";

        if(diffDate>0) rcptTim+=diffDate+"일 ";
        if(diffHour>0) rcptTim+=diffHour+"시간 ";
        rcptTim+=diffMin+"분";
        System.out.println(rcptTim);

        SrRcptTreat srRcptTreat = null;
        if(isSrRcptTreat(srRcptDto.getSrId()))
            srRcptTreat = srRcptTreatRepository.getOneBySrId(sr.getSrId());

        srRcptTreat = SrRcptTreat.builder()
                .srId(srRcptDto.getSrId())
                .recivrId(srRcptDto.getRecivrId())
                .rcptDscr(srRcptDto.getRcptDscr())
                .cmplPrdt(srRcptDto.getCmplPrdt())
                .treatBeginTim(srRcptDto.getTreatBeginTim())
                .rcptTim(rcptTim)
                .build();
        srRcptTreatRepository.save(srRcptTreat);

        saveNotfc(sr.getUsrId(), sr.getSrId(), "접수"
                , "/customer/sr/detail/"+sr.getSrId());

    }

    /**
     * srTreat 수정 / 저장
     * @param srTreatDto
     */
    @Override
    public void saveSrTreat(SrTreatDto srTreatDto) {
        Sr sr = srRepository.getOneBySrId(srTreatDto.getSrId());

        sr.setState("해결");
        srRepository.save(sr);

        SrRcptTreat srRcptTreat = srRcptTreatRepository.getOneBySrId(sr.getSrId());

        srRcptTreat.setOpetrId(srTreatDto.getOpetrId());
        srRcptTreat.setTreatDscr(srTreatDto.getTreatDscr());
        srRcptTreat.setTreatEndTim(srTreatDto.getTreatEndTim());
        srRcptTreat.setRqmtTim(srTreatDto.getRqmtTim());

        srRcptTreatRepository.save(srRcptTreat);

        saveNotfc(sr.getUsrId(), sr.getSrId(), "처리"
                , "/customer/sr/detail/"+sr.getSrId());

    }

    /**
     * srRcptTreat가 존재하는지 확인
     * @param srId
     * @return
     */
    @Override
    public boolean isSrRcptTreat(int srId) {
        return srRcptTreatRepository.existsBySrId(srId);
    }

    /**
     * sr 접수 삭제
     * @param srId
     */
    @Override
    public void deleteSrRcpt(int srId) {
        if(isSrRcptTreat(srId)){
            srRcptTreatRepository.deleteBySrId(srId);

            Sr sr = srRepository.getOneBySrId(srId);
            sr.setState("대기");
            srRepository.save(sr);

            saveNotfc(sr.getUsrId(), sr.getSrId(), "접수 취소"
                    , "/customer/sr/detail/"+sr.getSrId());
        }
        return;
    }

    /**
     * sr 처리 삭제
     * @param srId
     */
    @Override
    public void deleteSrTreat(int srId) {
        SrRcptTreat srRcptTreat;
        if(isSrRcptTreat(srId)){
            srRcptTreat = srRcptTreatRepository.getOneBySrId(srId);
            srRcptTreat.setOpetrId(null);
            srRcptTreat.setTreatDscr(null);
            srRcptTreat.setTreatEndTim(null);
            srRcptTreat.setRqmtTim(null);
            srRcptTreatRepository.save(srRcptTreat);

            Sr sr = srRepository.getOneBySrId(srId);
            sr.setState("진행중");
            srRepository.save(sr);

            saveNotfc(sr.getUsrId(), sr.getSrId(), "처리 취소"
                    , "/customer/sr/detail/"+sr.getSrId());
        }
        return;
    }

    /**
     * SR 접수, 처리 삭제 후 SR을 반려상태로 변환
     * @param srRjt
     */
    @Override
    public void rejectSr(SrRjt srRjt) {
        if(isSrRcptTreat(srRjt.getSrId())){
            srRcptTreatRepository.delete(srRcptTreatRepository.getOneBySrId(srRjt.getSrId()));
        }
        Sr sr = srRepository.getOneBySrId(srRjt.getSrId());
        sr.setState("반려");
        srRepository.save(sr);

        srRjtRepository.save(srRjt);
        saveNotfc(sr.getUsrId(), sr.getSrId(), "반려"
                , "/customer/sr/detail/"+sr.getSrId());

        return;
    }

    /**
     * 페이지 별 SR 조회
     * @param page
     * @param perPage
     * @param searchOptions
     * @return
     */
    @Override
    public List<SrSrRcptTreatDto> findSrs(int page, int perPage, SrDto searchOptions) {
        List<SrSrRcptTreatDto> result = srRepositorySupport.findSrsByOper(page, perPage, searchOptions);
        return result;
    }

    /**
     * 시스템 현황 막대그래프
     *
     * @param sysCd
     * @param reqGb
     * @param past
     * @param today
     * @return
     */
    @Override
    public List<SrCntBySysAndReqDto> srSystemStatusBarChart(int sysCd, String reqGb, LocalDateTime past, LocalDateTime today) {
        // LocalDateTime to dateTime convert
        Date end = Date.from(today.atZone(ZoneId.systemDefault()).toInstant());
        Date start = Date.from(past.atZone(ZoneId.systemDefault()).toInstant());

        List<SrCntBySysAndReqDto> result = srRepositorySupport.getCountBySysAndReqForChart(sysCd, reqGb, start, end);
        return result;
    }

    /**
     * 월 별 SR 합 꺾은선 그래프 데이터
     *
     * @param coCd
     * @param sysCd
     * @return
     */
    @Override
    public List<SrCntPerMonthDto> srCntLineChart(String coCd, int sysCd) {

        // 현재 날짜부터 6개월 전 Date
        LocalDate today = LocalDate.now();
        LocalDate sixMonthsAgoDate = today.minusMonths(6);
        LocalDate past = LocalDate.of(sixMonthsAgoDate.getYear(), sixMonthsAgoDate.getMonth(), 1);

        // LocalDate to Date convert
        Date start = Date.from(past.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());

        return srRepositorySupport.getCountPerMonth(coCd, sysCd, start ,end);
    }

    /**
     * 월 별 접수시간 평균, 처리시간 평균 Line Chart
     *
     * @param coCd
     * @param sysCd
     * @return
     */
    @Override
    public RqmtTimeLineChartResource srAvgTimeLineChart(String coCd, int sysCd) {
        // 평균 접수시간 데이터
        List<IAvgTimeDto> avgRcptTimes = srRepository.getAvgRcptRqmtTime(coCd, sysCd);
        // 평균 처리시간 데이터
        List<IAvgTimeDto> avgTreatTimes = srRepository.getAvgTreatRqmtTime(coCd, sysCd);

        int maxSize = avgRcptTimes.size();
        String[] label = new String[maxSize];
        double[] rcptTimeData = new double[maxSize];
        double[] treatTimeData = new double[maxSize];

        for(int index=0; index<maxSize; index++) {
            IAvgTimeDto avgRcptTime = avgRcptTimes.get(index);
            IAvgTimeDto avgTreatTime = avgTreatTimes.get(index);

            // YYYY-MM 데이터 라벨
            label[index] = avgRcptTime.getYearMonth();

            // day, hour, minute 데이터를 hour 데이터로 변환
            double rcptTime = avgRcptTime.getDay()*24 + avgRcptTime.getHour() + avgRcptTime.getMinute()/60.0;
            double treatTime = avgTreatTime.getDay()*24 + avgTreatTime.getHour() + avgTreatTime.getMinute()/60.0;
            rcptTimeData[index] = rcptTime;
            treatTimeData[index] = treatTime;
        }

        RqmtTimeLineChartResource result = RqmtTimeLineChartResource.builder()
                .label(label)
                .rcptData(rcptTimeData)
                .treatData(treatTimeData)
                .build();
        return result;
    }

    /**
     * 반려된 SR 반려 취소 -> 대기 상태로 변경
     * @param srId
     */
    @Override
    public void cancelReturned(int srId) {
        Sr sr = srRepository.getOneBySrId(srId);
        sr.setState("대기");
        srRepository.save(sr);

        srRjtRepository.deleteBySrId(srId);

        saveNotfc(sr.getUsrId(), sr.getSrId(), "반려 취소"
                , "/customer/sr/detail/"+sr.getSrId());

    }

    /**
     * 반려된 SR 조회
     * @param srId
     */
    @Override
    public SrRjt getRejectSrBySrId(int srId) {
        SrRjt srRjt = srRjtRepository.findBySrId(srId);
        return srRjt;
    }

    /**
     * notfc 알림 저장
     * @param usrId
     * @param srId
     * @param state
     * @param trget_url
     */
    private void saveNotfc(String usrId, int srId, String state, String trget_url) {
        Notfc notfc = Notfc.builder()
                .usrId(usrId)
                .trgetId(srId)
                .state(state)
                .trgetUrl(trget_url)
                .notfcTim(new Date())
                .func("sr")
                .build();
        notfcRepository.save(notfc);
    }

}
