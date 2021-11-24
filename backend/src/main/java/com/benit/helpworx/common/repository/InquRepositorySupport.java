package com.benit.helpworx.common.repository;

import com.benit.helpworx.common.entity.QRltmInqu;
import com.benit.helpworx.common.entity.RltmInqu;
import com.benit.helpworx.common.entity.Sr;
import com.benit.helpworx.inqu.dto.InquListDto;
import com.benit.helpworx.inqu.dto.OperInquListDto;
import com.benit.helpworx.sr.dto.SrCntBySysAndReqDto;
import com.benit.helpworx.sr.dto.SrCntPerMonthDto;
import com.benit.helpworx.sr.dto.SrDto;
import com.benit.helpworx.sr.dto.SrSrRcptTreatDto;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
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
import static com.benit.helpworx.common.entity.QRltmInqu.rltmInqu;
import static com.benit.helpworx.common.entity.QInquLog.inquLog;

@Repository
public class InquRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    public InquRepositorySupport(JPAQueryFactory queryFactory) {
        super(RltmInqu.class);
        this.queryFactory = queryFactory;
    }


    /**
     * 고객 문의 목록 - 페이징
     */
    public List<RltmInqu> findInqus(int page, int perPage, InquListDto inquListDto) {
        int startPage = (page - 1) * perPage;

        if (inquListDto.getState().equals("전체")) {
            return queryFactory.select(rltmInqu).from(rltmInqu, inquLog)
                    .where(rltmInqu.usrId.eq(inquListDto.getUsrId()),
                            rltmInqu.sysCd.eq(inquListDto.getSysCd()),
                            inquLog.inquId.eq(rltmInqu.inquId),
                            inquLog.inquDscr.contains(inquListDto.getDscr())
                    )
                    .distinct()
                    .orderBy(rltmInqu.beginTim.desc())
                    .offset(startPage)
                    .limit(perPage)
                    .fetch();
        } else {
            return queryFactory.select(rltmInqu).from(rltmInqu, inquLog)
                    .where(rltmInqu.usrId.eq(inquListDto.getUsrId()),
                            rltmInqu.sysCd.eq(inquListDto.getSysCd()),
                            rltmInqu.state.eq(inquListDto.getState()),
                            inquLog.inquId.eq(rltmInqu.inquId),
                            inquLog.inquDscr.contains(inquListDto.getDscr())
                    )
                    .distinct()
                    .orderBy(rltmInqu.beginTim.desc())
                    .offset(startPage)
                    .limit(perPage)
                    .fetch();
        }

}

    /**
     * 고객 - 조회한 문의 총 개수 리턴
     * @param inquListDto
     * @return
     */
    public long getInquListCount(InquListDto inquListDto) {
        if (inquListDto.getState().equals("전체")) {
            return queryFactory.select(rltmInqu).from(rltmInqu, inquLog)
                    .where(rltmInqu.usrId.eq(inquListDto.getUsrId()),
                            rltmInqu.sysCd.eq(inquListDto.getSysCd()),
                            inquLog.inquId.eq(rltmInqu.inquId),
                            inquLog.inquDscr.contains(inquListDto.getDscr())
                    )
                    .distinct()
                    .fetchCount();
        } else {
            return queryFactory.select(rltmInqu).from(rltmInqu, inquLog)
                    .where(rltmInqu.usrId.eq(inquListDto.getUsrId()),
                            rltmInqu.sysCd.eq(inquListDto.getSysCd()),
                            rltmInqu.state.eq(inquListDto.getState()),
                            inquLog.inquId.eq(rltmInqu.inquId),
                            inquLog.inquDscr.contains(inquListDto.getDscr())
                    )
                    .distinct()
                    .fetchCount();
        }
    }

    /**
     * 운영자 문의 목록 - 페이징
     */
    public List<RltmInqu> findOperInqus(int page, int perPage, OperInquListDto inquListDto) {
        int startPage = (page - 1) * perPage;

        if (inquListDto.getState().equals("전체")) {
            return queryFactory.select(rltmInqu).from(rltmInqu, inquLog)
                    .where(rltmInqu.coCd.eq(inquListDto.getCoCd()),
                            rltmInqu.sysCd.eq(inquListDto.getSysCd()),
                            inquLog.inquId.eq(rltmInqu.inquId),
                            inquLog.inquDscr.contains(inquListDto.getDscr())
                    )
                    .distinct()
                    .orderBy(rltmInqu.beginTim.desc())
                    .offset(startPage)
                    .limit(perPage)
                    .fetch();
        } else {
            return queryFactory.select(rltmInqu).from(rltmInqu, inquLog)
                    .where(rltmInqu.coCd.eq(inquListDto.getCoCd()),
                            rltmInqu.sysCd.eq(inquListDto.getSysCd()),
                            rltmInqu.state.eq(inquListDto.getState()),
                            inquLog.inquId.eq(rltmInqu.inquId),
                            inquLog.inquDscr.contains(inquListDto.getDscr())
                    )
                    .distinct()
                    .orderBy(rltmInqu.beginTim.desc())
                    .offset(startPage)
                    .limit(perPage)
                    .fetch();
        }

    }

    /**
     * 운영자 - 조회한 문의 총 개수 리턴
     * @param inquListDto
     * @return
     */
    public long getOperInquListCount(OperInquListDto inquListDto) {
        if (inquListDto.getState().equals("전체")) {
            return queryFactory.select(rltmInqu).from(rltmInqu, inquLog)
                    .where(rltmInqu.coCd.eq(inquListDto.getCoCd()),
                            rltmInqu.sysCd.eq(inquListDto.getSysCd()),
                            inquLog.inquId.eq(rltmInqu.inquId),
                            inquLog.inquDscr.contains(inquListDto.getDscr())
                    )
                    .distinct()
                    .fetchCount();
        } else {
            return queryFactory.select(rltmInqu).from(rltmInqu, inquLog)
                    .where(rltmInqu.coCd.eq(inquListDto.getCoCd()),
                            rltmInqu.sysCd.eq(inquListDto.getSysCd()),
                            rltmInqu.state.eq(inquListDto.getState()),
                            inquLog.inquId.eq(rltmInqu.inquId),
                            inquLog.inquDscr.contains(inquListDto.getDscr())
                    )
                    .distinct()
                    .fetchCount();
        }
    }
}
