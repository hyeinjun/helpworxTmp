package com.benit.helpworx.main.controller;


import com.benit.helpworx.main.dto.*;
import com.benit.helpworx.main.service.OperatorMainService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = { "*" }, allowCredentials = "true")
@RestController
@RequestMapping("")
public class OperatorMainController {

    @Autowired
    OperatorMainService operatorMainService;

    @ApiOperation(value = "시스템 별 전체 건수, 월별 건수 조회")
    @GetMapping("/sys/cnt/all-mm")
    public ResponseEntity getAllAndMnCntWithSysCd(@RequestParam String coCd, @RequestParam int sysCd){
        AllAndMnCntWithSysCdDto ret = operatorMainService.getAllAndMnCntWithSysCd(coCd, sysCd);
        return new ResponseEntity(ret, HttpStatus.OK);
    }

    @ApiOperation(value = "회사의 미완료 SR 목록")
    @GetMapping("/sr/incmp")
    public ResponseEntity getIncmpList(@RequestParam String coCd){
        List<IncmpSrDto> ret = operatorMainService.getIncmpList(coCd);
        return new ResponseEntity(ret, HttpStatus.OK);

    }

    @ApiOperation(value = "회사의 진행중인 실시간 문의 목록 - 7개")
    @GetMapping("/inqu/prgss")
    public ResponseEntity getPrgssInquList(@RequestParam String coCd){
        List<PrgssInquDto> ret = operatorMainService.getPrgssInquList(coCd);
        return new ResponseEntity(ret, HttpStatus.OK);

    }

    @ApiOperation(value = "전체 시스템 목록 조회")
    @GetMapping("/sys")
    public ResponseEntity getSystemInfo() {
        List<SysDto> result = operatorMainService.getAllSys();
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @ApiOperation(value = "전체 jobGB (업무구분) 목록 조회")
    @GetMapping("/jobgb")
    public ResponseEntity getJobGbInfo() {
        List<JobGbDto> result = operatorMainService.getAllJobGb();
        return new ResponseEntity(result, HttpStatus.OK);
    }
}
