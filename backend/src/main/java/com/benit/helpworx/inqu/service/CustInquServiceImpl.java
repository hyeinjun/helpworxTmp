package com.benit.helpworx.inqu.service;

import com.benit.helpworx.common.entity.InquLog;
import com.benit.helpworx.common.entity.RltmInqu;
import com.benit.helpworx.common.repository.InquLogRepository;
import com.benit.helpworx.common.repository.InquRepositorySupport;
import com.benit.helpworx.common.repository.RltmInquRepository;
import com.benit.helpworx.common.repository.SysRepository;
import com.benit.helpworx.inqu.dto.InquListDto;
import com.benit.helpworx.inqu.resource.SearchedRltmInquResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustInquServiceImpl implements CustInquService{
    @Autowired
    RltmInquRepository rltmInquRepository;

    @Autowired
    InquLogRepository inquLogRepository;

    @Autowired
    SysRepository sysRepository;

    @Autowired
    InquRepositorySupport inquRepositorySupport;

    /**
     * usrId, sysCd, state, dscr을 가지고 inqu를 가지고 옴.
     * @param page
     * @param perPage
     * @param inquListDto
     * @return
     */
    @Override
    public List<RltmInqu> findInquByInquListDtoPerPage(int page, int perPage, InquListDto inquListDto) {

        List<RltmInqu> ret = inquRepositorySupport.findInqus(page, perPage, inquListDto);

        return ret;
    }

    @Override
    public long getInquListCount(InquListDto inquListDto) {
        long count = inquRepositorySupport.getInquListCount(inquListDto);
        return count;
    }
}
