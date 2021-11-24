package com.benit.helpworx.inqu.service;

import com.benit.helpworx.common.entity.InquLog;
import com.benit.helpworx.common.entity.RltmInqu;
import com.benit.helpworx.inqu.dto.InquLogDto;
import com.benit.helpworx.inqu.dto.RltmInquDto;
import com.benit.helpworx.inqu.dto.SysDto;
import com.querydsl.core.QueryResults;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface InquService {
    public int createInqu(RltmInquDto dto);

    public String findInquUsrId(int inquId);

    public void saveInquLog(InquLogDto dto);

    public List<RltmInqu> findAllListByUsrId(String usrId);

    public List<InquLogDto> findBefChatByInquId(int inquId);

    public int findOneByInquIdAndInquDscrAndTim(int inquId, String inquDscr, Date tim);

    public InquLog getLastLogByInquId(int inquId);

    public void deleteInqu(int inquId);

    public List<SysDto> getUsingSysList(String coCd);

    public RltmInqu findInquByInquId(int inquId);

    public void finInqu(int inquId);

    public void putLogStateCnfrmByInquId(int inquId);

    public int cntUnCnfrmByUsrId(String usrId);

    public int cntOperUnCnfrmByUsrId(String usrId);
}
