package com.benit.helpworx.common.repository;

import com.benit.helpworx.common.entity.Co;
import com.benit.helpworx.comng.dto.SearchCoInfoDto;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import static com.benit.helpworx.common.entity.QCo.co;

@RequiredArgsConstructor
public class CoRepositoryImpl implements CoRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    //private static final int BLOCK_PAGE_NUM_COUNT=5; // 블럭당 페이지 수
    private static final int PAGE_POST_COUNT=10; // 페이지당 보이는 게시글 수

    /*@Override
    public List<Co> findDynamicQuery(String coCd, String coNm) {
        BooleanBuilder builder=new BooleanBuilder();
        return queryFactory
                .selectFrom(co)
                .where(eqCoCd(coCd),eqCoNm(coNm))
                .fetch();
    }*/

    @Override
    public QueryResults<Co> findDynamicQuery(SearchCoInfoDto searchCoInfoDto) {

        int startPage=PAGE_POST_COUNT* (searchCoInfoDto.getPage()-1);

        return queryFactory
                .selectFrom(co)
                .where(eqCoCd(searchCoInfoDto.getCocd()),eqCoNm(searchCoInfoDto.getCoNm()))
                .offset(startPage)
                .limit(PAGE_POST_COUNT)
                .orderBy(co.coCd.asc())
                .fetchResults();

    }

    private BooleanExpression eqCoCd(String coCd){

        if(!StringUtils.hasText(coCd)){
            return null;
        }

        return co.coCd.contains(coCd);
    }

    private BooleanExpression eqCoNm(String coNm){

        if(!StringUtils.hasText(coNm)){
            return null;
        }

        return co.coNm.contains(coNm);
    }
}
