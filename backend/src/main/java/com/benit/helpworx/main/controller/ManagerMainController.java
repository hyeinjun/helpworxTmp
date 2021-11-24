package com.benit.helpworx.main.controller;

import com.benit.helpworx.common.jwt.JwtTokenUtil;
import com.benit.helpworx.main.dto.EndSchdCoDto;
import com.benit.helpworx.main.dto.MainCoListDto;
import com.benit.helpworx.main.dto.MainInfoModReqDto;
import com.benit.helpworx.main.dto.NewContCoDto;
import com.benit.helpworx.main.resource.EndSchdCoResource;
import com.benit.helpworx.main.resource.MainCoListResource;
import com.benit.helpworx.main.resource.MainInfoModReqResource;
import com.benit.helpworx.main.resource.NewContCoResource;
import com.benit.helpworx.main.service.ManagerMainService;
import com.benit.helpworx.custmng.service.CustomerManagementService;
import io.swagger.annotations.ApiOperation;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = {"*"},allowCredentials = "true")
@RestController
@RequestMapping(value="")
public class ManagerMainController {

    @Autowired
    private ManagerMainService managerMainService;

    @Autowired
    private CustomerManagementService customerManagementService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 고객사 목록
     *
     * @param header
     * @return
     */
    @ApiOperation("고객사 목록")
    @GetMapping("/main/co")
    public ResponseEntity mainListCompany(@RequestHeader final HttpHeaders header){

        List<MainCoListDto> mainCoListDtos= managerMainService.mainListCompanyService();
        List<MainCoListResource> mainCoListResources=new ArrayList<>();

        for(MainCoListDto mainCoListDto:mainCoListDtos){
            mainCoListResources.add(mainCoListDto.toListResource
                    (customerManagementService.countCustomer(mainCoListDto.getCoCd())));
        }

        return new ResponseEntity(mainCoListResources, HttpStatus.OK);
    }

    @ApiOperation("종료 예정 고객사 목록")
    @GetMapping("/main/endco")
    @ResponseBody
    public ResponseEntity mainEndSchdCompany(@RequestHeader final HttpHeaders header) throws ParseException {

        List<EndSchdCoDto> endSchdCoDtos =managerMainService.endScheduledCompanyService();
        List<EndSchdCoResource> endSchdCoResources =new ArrayList<>();

        for(EndSchdCoDto endSchdCoDto : endSchdCoDtos){
            System.out.println(endSchdCoDto.getCoCd());
            endSchdCoResources.add(endSchdCoDto.toCoResource());
        }

        return new ResponseEntity(endSchdCoResources, HttpStatus.OK);

    }

    @ApiOperation("새 계약사 목록")
    @GetMapping("/main/newco")
    @ResponseBody
    public ResponseEntity mainNewContCompany(@RequestHeader final HttpHeaders header) throws ParseException {

        List<NewContCoDto> newContCoDtos =managerMainService.newContractCompanyService();
        List<NewContCoResource> newContCoResources =new ArrayList<>();

        for(NewContCoDto newContCoDto : newContCoDtos){
            newContCoResources.add(newContCoDto.toCoResource());
        }

        return new ResponseEntity(newContCoResources, HttpStatus.OK);

    }

    @ApiOperation("완료되지 않은 요청 목록")
    @GetMapping("/main/info-req")
    @ResponseBody
    public ResponseEntity mainInfoModReq(@RequestHeader final HttpHeaders header) throws ParseException {

        List<MainInfoModReqDto> mainInfoModReqDtos =managerMainService.mainInfoReqListService();
        List<MainInfoModReqResource> mainInfoModReqResources =new ArrayList<>();

        for(MainInfoModReqDto mainInfoModReqDto:mainInfoModReqDtos){
            mainInfoModReqResources.add(mainInfoModReqDto.toResource());
        }

        return new ResponseEntity(mainInfoModReqResources, HttpStatus.OK);

    }
}
