package com.benit.helpworx.infomod.service;

import com.benit.helpworx.infomod.dto.*;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
public interface MngInfoModReqService {
    public void updateInfoModReq(InfoModReqStateDto infoModReqStateDto);
    public void deleteInfoModReq(int reqId);
    public InfoModReqListDto listInfoModReq(SearchInfoModReqDto searchInfoModReqDto);
    public InfoModReqDto detailInfoModReq(int reqId);
    public void updateNotfc(NotfcDto notfcDto) throws ParseException;
}
