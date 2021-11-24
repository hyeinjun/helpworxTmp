package com.benit.helpworx.common.repository;

import com.benit.helpworx.common.entity.Sr;
import com.benit.helpworx.sr.dto.SrCntBySysAndReqDto;
import com.benit.helpworx.sr.dto.SrCntPerMonthDto;
import com.benit.helpworx.sr.dto.SrDto;
import com.benit.helpworx.sr.dto.SrSrRcptTreatDto;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;


import java.util.Date;
import java.util.List;

import static com.benit.helpworx.common.entity.QCo.co;
import static com.benit.helpworx.common.entity.QSr.sr;
import static com.benit.helpworx.common.entity.QSrRcptTreat.srRcptTreat;
import static com.benit.helpworx.common.entity.QTempDate.tempDate;

@Repository
public class SrRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    public SrRepositorySupport(JPAQueryFactory queryFactory) {
        super(Sr.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 주어진 조건을 만족하는 회사별 SR 건의 합
     * 조건 : 시스템, 요청, 등록시간 : 최근 1년
     *
     * @param sysCd
     * @param reqGb
     * @param start
     * @param end
     * @return
     */
    public List<SrCntBySysAndReqDto> getCountBySysAndReqForChart(int sysCd, String reqGb, Date start, Date end) {
       return queryFactory
                .select(Projections.constructor(SrCntBySysAndReqDto.class, co.coCd, sr.srId.count().as("count")))
                .from(sr)
                .rightJoin(co).on(sr.coCd.eq(co.coCd), sr.sysCd.eq(sysCd), sr.reqGb.eq(reqGb), sr.regTim.between(start,end))
                .fetchJoin()
                .where(co.useYn.eq(true))
                .groupBy(co.coCd)
                .orderBy(co.coCd.asc())
                .fetch();
    }

    /**
     * 회사,시스템 별 월별 SR 합
     *
     * @param coCd
     * @param sysCd
     * @param start Date type
     * @param end   Date type
     * @return
     */
    public List<SrCntPerMonthDto>  getCountPerMonth(String coCd, int sysCd, Date start, Date end) {
        // timestamp 인 temp_date.date 를 "YYYY-MM' String format으로 변경
        StringTemplate formattedTempDate = Expressions.stringTemplate(
                "to_char({0}, '{1s}')"
                ,tempDate.date
                , ConstantImpl.create("YYYY-MM")
        );
        // timestamp 인 sr.reg_tim 을 "YYYY-MM-DD' String format으로 변경
        StringTemplate formattedRegTim = Expressions.stringTemplate(
                "to_char({0}, '{1s}')"
                ,sr.regTim
                , ConstantImpl.create("YYYY-MM-DD"));

        return queryFactory
                .select(Projections.constructor(SrCntPerMonthDto.class, formattedTempDate.as("month"),sr.srId.count()))
                .from(tempDate)
                .leftJoin(sr)
                .on(tempDate.date.stringValue().eq(formattedRegTim), sr.coCd.eq(coCd), sr.sysCd.eq(sysCd))
                .fetchJoin()
                .where(tempDate.date.between(start, end))
                .groupBy(formattedTempDate)
                .orderBy(formattedTempDate.asc())
                .fetch();
    }

    /**
     * 고객 SR 조회 시 총 건수
     *
     * @param searchOptions
     * @return
     */
    public long getSrListCount(SrDto searchOptions) {
        return queryFactory.selectFrom(sr)
                .where(sr.coCd.eq(searchOptions.getCoCd()),
                        sr.sysCd.eq(searchOptions.getSysCd()),
                        eqJobGbId(searchOptions.getJobGbId()),
                        eqUsrId(searchOptions.getUsrId()),
                        eqReqGb(searchOptions.getReqGb()),
                        eqState(searchOptions.getState()),
                        eqSbjct(searchOptions.getSbjct()),
                        eqDscr(searchOptions.getDscr()))
                .fetchCount();
    }

    /**
     * 고객의 SR 조회 + 페이징
     */
    public List<Sr> findSrs(int page, int perPage, SrDto searchOptions) {
        int startPage = (page-1)*perPage;
        return queryFactory.selectFrom(sr)
                .where(sr.coCd.eq(searchOptions.getCoCd()),
                        sr.sysCd.eq(searchOptions.getSysCd()),
                        eqJobGbId(searchOptions.getJobGbId()),
                        eqUsrId(searchOptions.getUsrId()),
                        eqReqGb(searchOptions.getReqGb()),
                        eqState(searchOptions.getState()),
                        eqSbjct(searchOptions.getSbjct()),
                        eqDscr(searchOptions.getDscr()))
                .orderBy(sr.regTim.desc())
                .offset(startPage)
                .limit(perPage)
                .fetch();
    }

    /**
     * 운영자의 SR 목록 검색 API + 페이지네이션
     *
     * @param page
     * @param perPage
     * @param searchOptions
     * @return
     */
    public List<SrSrRcptTreatDto> findSrsByOper(int page, int perPage, SrDto searchOptions) {
        int startPage = (page-1)*perPage;
        return queryFactory
                .select(Projections.constructor(SrSrRcptTreatDto.class
                        ,sr.srId,sr.coCd,sr.sysCd,sr.jobGbId,sr.usrId, sr.reqGb,sr.sbjct, sr.dscr,sr.state,sr.regTim
                        ,srRcptTreat.srRcptTreatId,srRcptTreat.recivrId,srRcptTreat.rcptDscr,srRcptTreat.cmplPrdt,srRcptTreat.opetrId,srRcptTreat.treatDscr,srRcptTreat.treatBeginTim,srRcptTreat.treatEndTim,srRcptTreat.rqmtTim))
                .from(sr)
                .where(sr.coCd.eq(searchOptions.getCoCd()),
                        sr.sysCd.eq(searchOptions.getSysCd()),
                        eqJobGbId(searchOptions.getJobGbId()),
                        eqReqGb(searchOptions.getReqGb()),
                        eqState(searchOptions.getState()),
                        eqSbjct(searchOptions.getSbjct()),
                        eqDscr(searchOptions.getDscr()))
                .leftJoin(srRcptTreat).on(srRcptTreat.srId.eq(sr.srId))
                .orderBy(sr.regTim.desc())
                .offset(startPage)
                .limit(perPage)
                .fetch();
    }

    /*
        findSrs에서 조건들이
        필수 조건이 아닌 경우
        검색 조건에 0 or null이 들어오면 BooleanExpression으로 체크
        필수 조건 : coCd, sysCd
        선택 조건 : jobGbId, reqGb, state, sbjct, dscr, usrId
     */
    private BooleanExpression eqJobGbId(int jobGbId) {
        if(jobGbId==0) {
            return null;
        }

        return sr.jobGbId.eq(jobGbId);
    }

    private BooleanExpression eqUsrId(String usrId) {
        if(StringUtils.isEmpty(usrId)) {
            return null;
        }

        return  sr.usrId.eq(usrId);
    }

    private BooleanExpression eqReqGb(String reqGb) {
        if(StringUtils.isEmpty(reqGb)) {
            return null;
        }

        return sr.reqGb.eq(reqGb);
    }

    private BooleanExpression eqState(String state) {
        if(StringUtils.isEmpty(state)) {
            return null;
        }

        return sr.state.eq(state);
    }

    private BooleanExpression eqSbjct(String sbjct) {
        if(StringUtils.isEmpty(sbjct)) {
            return null;
        }

        return sr.sbjct.contains(sbjct);
    }

    private BooleanExpression eqDscr(String dscr) {
        if(StringUtils.isEmpty(dscr)) {
            return null;
        }

        return sr.dscr.contains(dscr);
    }


}
