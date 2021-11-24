package com.benit.helpworx.inqu.controller;

import com.benit.helpworx.common.entity.RltmInqu;
import com.benit.helpworx.inqu.dto.InquListDto;
import com.benit.helpworx.inqu.resource.SearchedRltmInquResource;
import com.benit.helpworx.inqu.service.CustInquService;
import io.swagger.annotations.ApiOperation;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("")
public class CustInquController {
    @Autowired
    CustInquService custInquService;

    /**
     * 고객이 자신이 작성한 문의를 조회
     * @param usrId
     * @param sysCd
     * @param state
     * @param dscr
     * @return
     */
    @ApiOperation(value = "고객 - 자신의 실시간 문의 리스트 조회")
    @GetMapping("/inqu/find/usr-sys-state-dscr")
    public ResponseEntity findInquBySysAndStateAndDscr(@RequestParam String usrId, @RequestParam int sysCd,
                                                       @RequestParam String state, @RequestParam String dscr,
                                                       @RequestParam int page, @RequestParam int perPage){

        InquListDto inquListDto = InquListDto.builder()
                .usrId(usrId)
                .sysCd(sysCd)
                .state(state)
                .dscr(dscr).build();

        List<RltmInqu> contents = custInquService.findInquByInquListDtoPerPage(page, perPage, inquListDto);
        long totalCount = custInquService.getInquListCount(inquListDto);

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

}
