package com.benit.helpworx.inqu.controller;

import com.benit.helpworx.common.entity.RltmInqu;
import com.benit.helpworx.common.jwt.JwtTokenUtil;
import com.benit.helpworx.inqu.dto.InquListDto;
import com.benit.helpworx.inqu.dto.LogListDto;
import com.benit.helpworx.inqu.dto.OperInquListDto;
import com.benit.helpworx.inqu.resource.LogListResource;
import com.benit.helpworx.inqu.resource.NewInquResource;
import com.benit.helpworx.inqu.resource.SearchedRltmInquResource;
import com.benit.helpworx.inqu.service.OperatorInquService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ResponseHeader;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("")
public class OperatorInquController {
    @Autowired
    OperatorInquService operatorInquService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    /**
     * 회사의 모든 문의 조회
     * @param coCd
     * @param sysCd
     * @param state
     * @param dscr
     * @return
     */
    @ApiOperation(value = "운영자 - 실시간 문의 리스트 조회")
    @GetMapping("/inqu/find/co-sys-state-dscr")
    public ResponseEntity findInqus(@RequestParam String coCd, @RequestParam int sysCd,
                                    @RequestParam String state, @RequestParam String dscr,
                                    @RequestParam int page, @RequestParam int perPage){
        OperInquListDto inquListDto = OperInquListDto.builder()
                .coCd(coCd)
                .sysCd(sysCd)
                .state(state)
                .dscr(dscr).build();
        List<RltmInqu> contents = operatorInquService.findInqus(page, perPage, inquListDto);
        long totalCount = operatorInquService.getInquListCount(inquListDto);

        JSONObject result = new JSONObject();
        result.put("result", true);

        JSONObject data = new JSONObject();
        data.put("contents", contents);

        JSONObject pagination = new JSONObject();
        pagination.put("page", page);
        pagination.put("totalCount", totalCount);

        data.put("pagination", pagination);
        result.put("data", data);

        return  new ResponseEntity(result, HttpStatus.OK);
    }

    @ApiOperation(value = "운영자가 답변중인 미종료된 문의 목록 조회")
    @GetMapping("/inqu/find/answrr")
    public ResponseEntity findInqusByAnswrr(@RequestHeader final HttpHeaders header){
        String accessToken= jwtTokenUtil.resolveToken(header);
        String usrId=jwtTokenUtil.extractUsrIdAccessToken(accessToken);

        List<LogListDto> list = operatorInquService.findInqusByAnswrr(usrId);
        List<LogListResource> ret = new ArrayList<>();
        list.forEach(e -> {
            LogListResource resource = e.toResource();
            resource.setUncnfrmCnt(operatorInquService.cntUncnfrmLogByInquId(e.getInquId(), usrId));
            ret.add(resource);
        });

        return  new ResponseEntity(ret, HttpStatus.OK);
    }

    @ApiOperation(value = "아직 답변자가 없는 문의 목록 조회")
    @GetMapping("/inqu/find/new")
    public ResponseEntity findNewInqus(@RequestHeader final HttpHeaders header){
        String accessToken= jwtTokenUtil.resolveToken(header);
        String coCd=jwtTokenUtil.extractCoCd(accessToken);
        List<RltmInqu> list = operatorInquService.findnewInqus(coCd);
        List<NewInquResource> ret = new ArrayList<>();

        list.forEach(e -> {
            ret.add(e.toNewInquResource());
        });

        return  new ResponseEntity(ret, HttpStatus.OK);
    }

    @ApiOperation(value = "문의 답변자 설정")
    @GetMapping("/inqu/start/ans")
    public ResponseEntity startAns(@RequestHeader final HttpHeaders header, int inquId){
        String accessToken= jwtTokenUtil.resolveToken(header);
        String usrId=jwtTokenUtil.extractUsrIdAccessToken(accessToken);

        operatorInquService.startAns(usrId, inquId);

        return  new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "아직 답변자가 없는 문의 개수 리턴")
    @GetMapping("/inqu/new/cnt")
    public ResponseEntity getNewInquCnt(@RequestHeader final HttpHeaders header){
        String accessToken= jwtTokenUtil.resolveToken(header);
        String coCd=jwtTokenUtil.extractCoCd(accessToken);

        int cnt = operatorInquService.getNewInquCnt(coCd);

        return  new ResponseEntity(cnt, HttpStatus.OK);
    }
}
