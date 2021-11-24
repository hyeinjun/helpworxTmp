package com.benit.helpworx.infomod.service;

import com.benit.helpworx.infomod.dto.InfoModReqDto;
import com.benit.helpworx.infomod.dto.InfoModReqListDto;
import com.benit.helpworx.infomod.dto.SearchInfoModReqDto;
import com.benit.helpworx.infomod.dto.UpdateInfoModReqDto;
import org.springframework.stereotype.Service;


import java.text.ParseException;
import java.util.List;

@Service
public interface CustInfoModReqService {
    public void insertInfoModReq(InfoModReqDto infoModReqDto) throws ParseException;
    public void updateInfoModReq(UpdateInfoModReqDto updateInfoModReqDto);
    public void deleteInfoModReq(int reqId);
    public InfoModReqListDto listInfoModReq(SearchInfoModReqDto searchInfoModReqDto);
    public InfoModReqDto detailInfoModReq(int reqId);
}
