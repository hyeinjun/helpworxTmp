package com.benit.helpworx.common.repository;

import com.benit.helpworx.common.entity.Co;
import com.benit.helpworx.comng.dto.SearchCoInfoDto;
import com.querydsl.core.QueryResults;

public interface CoRepositoryCustom {
    QueryResults<Co> findDynamicQuery(SearchCoInfoDto searchCoInfoDto);
}
