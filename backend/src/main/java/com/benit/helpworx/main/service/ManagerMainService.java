package com.benit.helpworx.main.service;

import com.benit.helpworx.main.dto.EndSchdCoDto;
import com.benit.helpworx.main.dto.MainCoListDto;
import com.benit.helpworx.main.dto.MainInfoModReqDto;
import com.benit.helpworx.main.dto.NewContCoDto;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
public interface ManagerMainService {
    List<MainCoListDto> mainListCompanyService();
    List<NewContCoDto> newContractCompanyService() throws ParseException;
    List<EndSchdCoDto> endScheduledCompanyService() throws ParseException;
    List<MainInfoModReqDto> mainInfoReqListService();
}
