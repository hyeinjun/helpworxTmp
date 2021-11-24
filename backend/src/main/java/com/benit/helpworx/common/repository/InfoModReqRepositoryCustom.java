package com.benit.helpworx.common.repository;

import com.benit.helpworx.common.entity.InfoModReq;
import com.benit.helpworx.infomod.dto.SearchInfoModReqDto;
import com.querydsl.core.QueryResults;

public interface InfoModReqRepositoryCustom {
    QueryResults<InfoModReq> findDynamicQuery(SearchInfoModReqDto searchInfoModReqDto);
    QueryResults<InfoModReq> findDynamicQueryMng(SearchInfoModReqDto searchInfoModReqDto);
}
