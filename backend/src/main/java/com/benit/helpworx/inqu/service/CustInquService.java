package com.benit.helpworx.inqu.service;

import com.benit.helpworx.common.entity.RltmInqu;
import com.benit.helpworx.inqu.dto.InquListDto;
import com.benit.helpworx.inqu.resource.SearchedRltmInquResource;

import java.util.List;

public interface CustInquService {
    public List<RltmInqu> findInquByInquListDtoPerPage(int page, int perPage, InquListDto inquListDto);
    public long getInquListCount(InquListDto inquListDto);
}
