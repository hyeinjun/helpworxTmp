package com.benit.helpworx.main.controller;

import com.benit.helpworx.common.entity.Sys;
import com.benit.helpworx.main.dto.AllAndMnCntWithSysCdDto;
import com.benit.helpworx.main.dto.IncmpSrDto;
import com.benit.helpworx.main.dto.IncmpSrWithCoCdDto;
import com.benit.helpworx.main.resource.SystemResource;
import com.benit.helpworx.main.service.CustMainService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = { "*" }, allowCredentials = "true")
@RestController
@RequestMapping("")
public class CustMainController {
    @Autowired
    CustMainService custMainService;

    @ApiOperation(value = "회사의 시스템 목록 조회")
    @GetMapping("/sys/coCd")
    public ResponseEntity getSysNmByCoCd(@RequestParam String coCd){
        List<Sys> sysList = custMainService.getSysNmByCoCd(coCd);

        List<SystemResource> result = new ArrayList<>();
        sysList.forEach(e ->
                result.add(SystemResource
                .builder()
                .value(e.getSysCd())
                .text(e.getSysNm())
                .build()));
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @ApiOperation(value = "회사의 시스템 별 전체 건수, 월별 건수 조회")
    @GetMapping("/sys/cnt")
    public ResponseEntity getAllAndMnCntWithSysCd(@RequestParam String coCd, @RequestParam int sysCd){
        AllAndMnCntWithSysCdDto ret = custMainService.getAllAndMnCntWithSysCdAndCoCd(sysCd, coCd);
        return new ResponseEntity(ret, HttpStatus.OK);

    }

    @ApiOperation(value = "나의 미완료 SR 목록")
    @GetMapping("/mysr/incmp")
    public ResponseEntity getIncmpListWithUsrId(@RequestParam String usrId){
        List<IncmpSrDto> ret = custMainService.getIncmpListWithUsrId(usrId);
        return new ResponseEntity(ret, HttpStatus.OK);

    }

    @ApiOperation(value = "회사의 미완료 SR 목록")
    @GetMapping("/cosr/incmp")
    public ResponseEntity getIncmpListWithCoCd(@RequestParam String coCd){
        List<IncmpSrWithCoCdDto> ret = custMainService.getIncmpListWithCoCd(coCd);
        return new ResponseEntity(ret, HttpStatus.OK);

    }

}
