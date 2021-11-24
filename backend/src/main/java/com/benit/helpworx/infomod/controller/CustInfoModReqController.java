package com.benit.helpworx.infomod.controller;

import com.benit.helpworx.common.jwt.JwtTokenUtil;
import com.benit.helpworx.infomod.dto.InfoModReqDto;
import com.benit.helpworx.infomod.dto.InfoModReqListDto;
import com.benit.helpworx.infomod.dto.SearchInfoModReqDto;
import com.benit.helpworx.infomod.resource.InfoModReqListResource;
import com.benit.helpworx.infomod.resource.InfoModReqResource;
import com.benit.helpworx.infomod.resource.UpdateInfoModReqResource;
import com.benit.helpworx.infomod.service.CustInfoModReqService;
import io.swagger.annotations.ApiOperation;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.header.Header;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = {"*"}, allowCredentials = "true")
@RestController
@RequestMapping(value="")
public class CustInfoModReqController {

    @Autowired
    private CustInfoModReqService custInfoModReqService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @ApiOperation(value="고객 - 목록 조회")
    @GetMapping("/cust/info-mod/list")
    public ResponseEntity listInfoModReq(@RequestParam(required = false) String state, @RequestParam String usrId, @RequestParam(required = false) Integer page){

        System.out.println(state);
        SearchInfoModReqDto searchInfoModReqDto= SearchInfoModReqDto.builder()
                .state(state)
                .usrId(usrId)
                .page(page)
                .build();

        InfoModReqListDto infoModReqListDto=custInfoModReqService.listInfoModReq(searchInfoModReqDto);

        List<InfoModReqListResource> listResources=new ArrayList<>();

            for(InfoModReqDto infoModReqDto: infoModReqListDto.getInfoModReqDtos()){
                listResources.add(infoModReqDto.toListResource());
            }

        System.out.println("resut?");
        //toast 그리드가 요구하는 폼으로 전환
        JSONObject result =new JSONObject();
        result.put("result",true);

        JSONObject data = new JSONObject();
        data.put("contents", listResources);

        JSONObject pagination = new JSONObject();
        pagination.put("page", page);
        pagination.put("totalCount", infoModReqListDto.getTotalCount());

        data.put("pagination", pagination);
        result.put("data", data);

        return new ResponseEntity(result, HttpStatus.OK);
    }

    @ApiOperation(value="상세 조회")
    @GetMapping("/cust/info-mod/detail")
    public ResponseEntity detailInfoModReq(@RequestParam int reqId){

        InfoModReqDto infoModReqDto=custInfoModReqService.detailInfoModReq(reqId);

        InfoModReqResource result=infoModReqDto.toResource();

        return new ResponseEntity(result,HttpStatus.OK);
    }

    @ApiOperation(value="고객 - 등록")
    @PostMapping("/cust/info-mod")
    public ResponseEntity insertInfoModReq(@Valid @RequestBody InfoModReqResource body) throws ParseException {

        body.setReqTim(getCurrentTime());
        body.setState("대기");

        custInfoModReqService.insertInfoModReq(body.toDto());
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value="고객 - 수정")
    @PutMapping("/cust/info-mod")
    public ResponseEntity modifyInfoModReq(@Valid @RequestBody UpdateInfoModReqResource body){
        custInfoModReqService.updateInfoModReq(body.toDto());
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value="삭제")
    @DeleteMapping("/cust/info-mod")
    public ResponseEntity deleteInfoModReq(@RequestParam Integer reqId){
        custInfoModReqService.deleteInfoModReq(reqId);
        return new ResponseEntity(HttpStatus.OK);
    }

    public String getCurrentTime() throws ParseException {
        Date date=new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String datestr=dateFormat.format(date);

        return datestr;
    }
}
