package com.benit.helpworx.infomod.controller;

import com.benit.helpworx.infomod.dto.InfoModReqDto;
import com.benit.helpworx.infomod.dto.InfoModReqListDto;
import com.benit.helpworx.infomod.dto.NotfcDto;
import com.benit.helpworx.infomod.dto.SearchInfoModReqDto;
import com.benit.helpworx.infomod.resource.InfoModReqListResource;
import com.benit.helpworx.infomod.resource.InfoModReqResource;
import com.benit.helpworx.infomod.resource.InfoModReqStateResource;
import com.benit.helpworx.infomod.service.MngInfoModReqService;
import io.swagger.annotations.ApiOperation;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class MngInfoModReqController {

    @Autowired
    private MngInfoModReqService mngInfoModReqService;

    @ApiOperation(value="관리자 - 목록 조회")
    @GetMapping("/mng/info-mod/list")
    public ResponseEntity listInfoModReq(@RequestParam(required = false) String state, @RequestParam(required = false) String usrId, @RequestParam(required = false) Integer page){

        SearchInfoModReqDto searchInfoModReqDto= SearchInfoModReqDto.builder()
                .state(state)
                .usrId(usrId)
                .page(page)
                .build();

        InfoModReqListDto infoModReqListDto=mngInfoModReqService.listInfoModReq(searchInfoModReqDto);

        List<InfoModReqListResource> listResources=new ArrayList<>();

        for(InfoModReqDto infoModReqDto: infoModReqListDto.getInfoModReqDtos()){
            listResources.add(infoModReqDto.toListResource());
        }

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
    @GetMapping("/mng/info-mod/detail")
    public ResponseEntity detailInfoModReq(@RequestParam int reqId){

        InfoModReqDto infoModReqDto=mngInfoModReqService.detailInfoModReq(reqId);

        InfoModReqResource result=infoModReqDto.toResource();

        return new ResponseEntity(result,HttpStatus.OK);
    }

    @ApiOperation(value="관리자 - 상태 수정")
    @PutMapping("/mng/info-mod")
    public ResponseEntity modifyInfoModReq(@Valid @RequestBody InfoModReqStateResource body) throws ParseException {

        mngInfoModReqService.updateInfoModReq(body.toDto());

        //reqId로 갱신된 req 정보를 받아온다.
        InfoModReqDto infoModReqDto=mngInfoModReqService.detailInfoModReq(body.getReqId());
        NotfcDto notfcDto=infoModReqDto.toNotfcDto();
        notfcDto.setNotfcTim(getCurrentTime());

        mngInfoModReqService.updateNotfc(notfcDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value="삭제")
    @DeleteMapping("/mng/info-mod")
    public ResponseEntity deleteInfoModReq(@RequestParam Integer reqId){
        mngInfoModReqService.deleteInfoModReq(reqId);
        return new ResponseEntity(HttpStatus.OK);
    }

    public String getCurrentTime() throws ParseException {
        Date date=new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String datestr=dateFormat.format(date);

        return datestr;
    }
}
