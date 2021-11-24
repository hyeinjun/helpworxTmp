package com.benit.helpworx.inqu.service;

import com.benit.helpworx.common.entity.RltmInqu;
import com.benit.helpworx.inqu.dto.InquListDto;
import com.benit.helpworx.inqu.dto.LogListDto;
import com.benit.helpworx.inqu.dto.OperInquListDto;
import com.benit.helpworx.inqu.resource.SearchedRltmInquResource;

import java.util.List;

public interface OperatorInquService {
    public List<RltmInqu> findInqus(int page, int perPage, OperInquListDto inquListDto);
    public long getInquListCount(OperInquListDto inquListDto);

    public List<LogListDto> findInqusByAnswrr(String usrId);

    public List<RltmInqu> findnewInqus(String coCd);

    public void startAns(String usrId, int inquId);

    public int getNewInquCnt(String coCd);

    public int cntUncnfrmLogByInquId(int inquId, String usrId);
}
