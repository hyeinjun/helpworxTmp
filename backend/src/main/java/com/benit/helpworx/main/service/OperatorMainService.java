package com.benit.helpworx.main.service;

import com.benit.helpworx.main.dto.*;

import java.util.List;

public interface OperatorMainService {
    public AllAndMnCntWithSysCdDto getAllAndMnCntWithSysCd(String coCd, int sysCd);
    public List<IncmpSrDto> getIncmpList(String coCd);
    public List<PrgssInquDto> getPrgssInquList(String coCd);

    public List<SysDto> getAllSys();
    public List<JobGbDto> getAllJobGb();
}
