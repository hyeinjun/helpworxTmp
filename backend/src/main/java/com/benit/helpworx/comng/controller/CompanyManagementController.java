package com.benit.helpworx.comng.controller;

import com.benit.helpworx.comng.dto.CoDto;
import com.benit.helpworx.comng.dto.CoListDto;
import com.benit.helpworx.comng.dto.CoNmDto;
import com.benit.helpworx.comng.dto.SearchCoInfoDto;
import com.benit.helpworx.comng.resource.CoNameListResource;
import com.benit.helpworx.comng.resource.CoDetailResource;
import com.benit.helpworx.comng.resource.CoListResource;
import com.benit.helpworx.comng.service.CompanyManagementService;
import com.benit.helpworx.custmng.service.CustomerManagementService;
import io.swagger.annotations.ApiOperation;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"*"}, allowCredentials = "true")
@RestController
@RequestMapping(value="")
public class CompanyManagementController {

    @Autowired
    private CompanyManagementService companyManagementService;

    @Autowired
    private CustomerManagementService customerManagementService;

    @ApiOperation(value = "회사 등록")
    @PostMapping ("/co")
    public ResponseEntity insertCompany(@Valid @RequestBody CoDetailResource body) throws ParseException {

        companyManagementService.insertCompany(body.toCoDto());
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 회사 목록 조회
     *
     * @param coCd
     * @param coNm
     * @param page
     * @return
     */
    @ApiOperation(value = "회사 목록 조회")
    @GetMapping ("/co/list")
    @ResponseBody
    public ResponseEntity listCompany(@RequestParam(required = false) String coCd, @RequestParam(required = false) String coNm,
                                                 @RequestParam(required = false) Integer page ) throws ParseException {

        SearchCoInfoDto searchCoInfoDto = SearchCoInfoDto.builder()
                .cocd(coCd)
                .coNm(coNm)
                .page(page)
                .build();

        CoListDto coListDto = companyManagementService.listCompany(searchCoInfoDto);

        List<CoListResource> listResources=new ArrayList<>();
        for(CoDto coDto:coListDto.getCoDtos()){
            listResources.add(coDto.toListResource(customerManagementService.countCustomer(coDto.getCoCd())));
        }

        //toast 그리드가 요구하는 폼으로 전환
        JSONObject result =new JSONObject();
        result.put("result",true);

        JSONObject data = new JSONObject();
        data.put("contents", listResources);

        JSONObject pagination = new JSONObject();
        pagination.put("page", page);
        pagination.put("totalCount", coListDto.getTotalCount());

        data.put("pagination", pagination);
        result.put("data", data);

        return new ResponseEntity(result, HttpStatus.OK);
    }

    @ApiOperation(value = "회사 상세 조회")
    @GetMapping ("/co/detail")
    @ResponseBody
    public ResponseEntity detailCompany(@RequestParam String coCd) throws ParseException {

        CoDto coDto=companyManagementService.detailCompany(coCd);
        CoDetailResource result = coDto.toDetailResource();

        return new ResponseEntity(result, HttpStatus.OK);
    }

    @ApiOperation(value = "회사 삭제")
    @DeleteMapping ("/co")
    public ResponseEntity deleteCompany(@RequestParam String coCd){

        //companyManagementService.deleteCompany(coCd);
        companyManagementService.changeCoUse(coCd);
        return new ResponseEntity(HttpStatus.OK);

    }

    @Deprecated
    @ApiOperation(value = "회사 수정")
    @PutMapping ("/co")
    public ResponseEntity updateCompany(@Valid @RequestBody JSONObject body){

        JSONObject result =new JSONObject();

        try {
            //수정 데이터를 하나씩 parse해서 보낸다.
            ArrayList<Map<String,Object> >arrayList=(ArrayList<Map<String,Object>>) body.get("updatedRows");
            int len=arrayList.size();

            for(int i=0;i<len;i++){
                    companyManagementService.updateCompany(CoDto.builder()
                            .coCd(arrayList.get(i).get("coCd").toString())
                            .coNm(arrayList.get(i).get("coNm").toString())
                            .useYn(Boolean.parseBoolean(arrayList.get(i).get("useYn").toString()))
                            .contBeginDt(arrayList.get(i).get("contBeginDt").toString())
                            .contEndDt(arrayList.get(i).get("contEndDt").toString())
                            .build());
                result.put("result",true);
                result.put("data", null);
            }

        }catch (Exception e){
            result.put("result",false);
            result.put("data", null);

        }finally {
            return new ResponseEntity(result,HttpStatus.OK);
        }

    }
    @ApiOperation(value = "회사 수정 - 상세페이지")
    @PutMapping ("/co/detail")
    public ResponseEntity updateCompanyDetail(@Valid @RequestBody CoDetailResource coDetailResource) throws ParseException {
        companyManagementService.updateCompany(coDetailResource.toCoDto());

        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "회사명 조회")
    @GetMapping ("/conm")
    @ResponseBody
    public ResponseEntity listCompanyName(){

        List<CoNmDto> coNmDtos= companyManagementService.listCompanyName();
        List<CoNameListResource> coNameListResources=new ArrayList<>();

        for(CoNmDto coNmDto:coNmDtos){
            coNameListResources.add(coNmDto.toListResource());
        }

        return new ResponseEntity<>(coNameListResources, HttpStatus.OK);
    }

    @ApiOperation(value = "모든 회사 코드 조회")
    @GetMapping ("/all/coCd")
    public ResponseEntity getAllCoCd(){
        List<String> ret = companyManagementService.getAllCoCd();
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }

}
