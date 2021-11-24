package com.benit.helpworx.sr.service;


import com.benit.helpworx.common.entity.SrRjt;
import com.benit.helpworx.sr.dto.*;
import com.benit.helpworx.sr.resource.RqmtTimeLineChartResource;

import java.time.LocalDateTime;
import java.util.List;

public interface SrOperatorService {
    public SrDto getSrWithSrId(int srId);

    public SrRcptTreatDto getSrRcptTreatWithSrId(int srId);

    public void saveSrRcpt(SrRcptDto srRcptTreatDto);

    public void saveSrTreat(SrTreatDto srRcptTreatDto);

    public boolean isSrRcptTreat(int srId);

    public void deleteSrRcpt(int srId);

    public void deleteSrTreat(int srId);

    public void rejectSr(SrRjt srRjt);

    public List<SrSrRcptTreatDto> findSrs(int page, int perPage, SrDto searchOptions);

    /**
     * 시스템 현황 막대그래프
     *
     * @param sysCd
     * @param reqGb
     * @param past
     * @param today
     * @return
     */
    public List<SrCntBySysAndReqDto> srSystemStatusBarChart(int sysCd, String reqGb, LocalDateTime past ,LocalDateTime today);

    /**
     * 월 별 SR 합 꺾은선 그래프 데이터
     * 
     * @param coCd
     * @param sysCd
     * @return
     */
    public List<SrCntPerMonthDto> srCntLineChart(String coCd, int sysCd);

    /**
     * 월 별 접수시간 평균, 처리시간 평균 Line Chart
     * @param coCd
     * @param sysCd
     * @return
     */
    public RqmtTimeLineChartResource srAvgTimeLineChart(String coCd, int sysCd);

    /**
     * 반려된 SR 반려 취소
     * @param srId
     */
    public void cancelReturned(int srId);

    /**
     * 반려된 SR 조회
     * @param srId
     */
    public SrRjt getRejectSrBySrId(int srId);
}
