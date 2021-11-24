package com.benit.helpworx.common.repository;

import com.benit.helpworx.common.entity.Usr;
import com.benit.helpworx.custmng.dto.SearchUsrInfoDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import static com.benit.helpworx.common.entity.QUsr.usr;

@RequiredArgsConstructor
public class UsrRepositoryImpl implements UsrRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private static final int PAGE_POST_COUNT=10;

    @Override
    public QueryResults<Usr> findDynamicQueryUser(SearchUsrInfoDto searchUsrInfoDto) {

        BooleanBuilder builder = new BooleanBuilder();
        int startPage = PAGE_POST_COUNT * (searchUsrInfoDto.getPage()-1);
        return queryFactory
                .selectFrom(usr)
                .where(eqCoCd(searchUsrInfoDto.getCoCd()),hasNm(searchUsrInfoDto.getNm()),hasUsrId(searchUsrInfoDto.getUsrId()))
                .offset(startPage)
                .limit(PAGE_POST_COUNT)
                .orderBy(usr.usrId.asc())
                .fetchResults();

    }

    private BooleanExpression eqCoCd(String coCd){
        if(StringUtils.isEmpty(coCd)){
            return null;
        }
        return usr.coCd.eq(coCd);
    }

    private BooleanExpression hasUsrId(String usrId) {
        if (!StringUtils.hasText(usrId)) {
            return null;
        }
        return usr.usrId.contains(usrId);
    }

    private BooleanExpression hasNm(String nm){
        if(!StringUtils.hasText(nm)){
            return null;
        }
        return usr.nm.contains(nm);

    }

}
