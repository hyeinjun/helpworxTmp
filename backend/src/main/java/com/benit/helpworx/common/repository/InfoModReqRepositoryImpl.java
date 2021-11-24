package com.benit.helpworx.common.repository;

import com.benit.helpworx.common.entity.InfoModReq;
import com.benit.helpworx.infomod.dto.SearchInfoModReqDto;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import static com.benit.helpworx.common.entity.QInfoModReq.infoModReq;

@RequiredArgsConstructor
public class InfoModReqRepositoryImpl implements InfoModReqRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private static final int PAGE_POST_COUNT=10;

    @Override
    public QueryResults<InfoModReq> findDynamicQuery(SearchInfoModReqDto searchInfoModReqDto) {
        int startPage=PAGE_POST_COUNT* (searchInfoModReqDto.getPage()-1);

        return queryFactory
                .selectFrom(infoModReq)
                .where(eqUsrId(searchInfoModReqDto.getUsrId()),eqState(searchInfoModReqDto.getState()))
                .offset(startPage)
                .limit(PAGE_POST_COUNT)
                .orderBy(infoModReq.reqTim.desc())
                .fetchResults();
    }

    @Override
    public QueryResults<InfoModReq> findDynamicQueryMng(SearchInfoModReqDto searchInfoModReqDto) {
        int startPage=PAGE_POST_COUNT* (searchInfoModReqDto.getPage()-1);

        return queryFactory
                .selectFrom(infoModReq)
                .where(likeUsrId(searchInfoModReqDto.getUsrId()),eqState(searchInfoModReqDto.getState()))
                .offset(startPage)
                .limit(PAGE_POST_COUNT)
                .orderBy(infoModReq.reqTim.desc())
                .fetchResults();
    }

    private BooleanExpression eqUsrId(String usrId){

        if(StringUtils.isEmpty(usrId)){
            return null;
        }

        return infoModReq.usrId.eq(usrId);
    }

    private BooleanExpression likeUsrId(String usrId){

        if(!StringUtils.hasText(usrId)){
            return null;
        }

        return infoModReq.usrId.contains(usrId);
    }
    private BooleanExpression eqState(String state){

        if(StringUtils.isEmpty(state)){
            return null;
        }

        return infoModReq.state.eq(state);
    }
}
