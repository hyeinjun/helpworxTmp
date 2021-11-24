package com.benit.helpworx.common.repository;

import com.benit.helpworx.common.entity.Usr;
import com.benit.helpworx.custmng.dto.SearchUsrInfoDto;
import com.querydsl.core.QueryResults;

public interface UsrRepositoryCustom {
    QueryResults<Usr> findDynamicQueryUser(SearchUsrInfoDto searchUsrInfoDto);
}
