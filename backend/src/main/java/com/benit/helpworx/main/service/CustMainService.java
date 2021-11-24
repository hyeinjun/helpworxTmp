package com.benit.helpworx.main.service;

import com.benit.helpworx.common.entity.Sys;
import com.benit.helpworx.main.dto.AllAndMnCntWithSysCdDto;
import com.benit.helpworx.main.dto.IncmpSrDto;
import com.benit.helpworx.main.dto.IncmpSrWithCoCdDto;

import java.util.List;

public interface CustMainService {

    List<Sys> getSysNmByCoCd(String coCd);

    AllAndMnCntWithSysCdDto getAllAndMnCntWithSysCdAndCoCd(int sysCd, String coCd);

    List<IncmpSrDto> getIncmpListWithUsrId(String usrId);

    List<IncmpSrWithCoCdDto> getIncmpListWithCoCd(String coCd);
}
