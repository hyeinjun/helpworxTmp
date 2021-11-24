package com.benit.helpworx.custmng.controller;

import com.benit.helpworx.comng.dto.CoDto;
import com.benit.helpworx.custmng.dto.SearchUsrInfoDto;
import com.benit.helpworx.custmng.dto.UsrAuthDto;
import com.benit.helpworx.custmng.dto.UsrDto;
import com.benit.helpworx.custmng.dto.UsrListDto;
import com.benit.helpworx.custmng.resource.UsrResource;
import com.benit.helpworx.custmng.service.CustomerManagementService;
import io.swagger.annotations.ApiOperation;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

@RestController
@CrossOrigin(origins = {"*"}, allowCredentials = "true")
@RequestMapping(value="")
public class CustomerManagementController {

    @Autowired
    private CustomerManagementService customerManagementService;

    /**
     * 고객 정보 등록
     *
     * @param usrResource
     * @return
     */
    @ApiOperation(value="고객 등록")
    @PostMapping("/customer")
    public ResponseEntity insertCustomer(@Valid @RequestBody UsrResource usrResource){
        customerManagementService.insertCustomer(usrResource.toDto());

        //고객 권한 등록
        customerManagementService.insertCustomerAuth(usrResource.toCustAuthDto());
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 고객 목록 조회
     *
     * @param coCd
     * @param usrId
     * @param nm
     * @param page
     * @return
     */
    @ApiOperation(value= "고객 목록 조회")
    @GetMapping("/customer/list")
    @ResponseBody
    public ResponseEntity listCustomer(@RequestParam(required = false) String coCd,
                                       @RequestParam(required = false) String usrId,
                                       @RequestParam(required = false) String nm,
                                       @RequestParam Integer page){

        SearchUsrInfoDto searchUsrInfoDto= SearchUsrInfoDto.builder()
                .coCd(coCd)
                .nm(nm)
                .usrId(usrId)
                .page(page)
                .build();

        UsrListDto usrListDto = customerManagementService.listCustomer(searchUsrInfoDto);

        List<UsrResource> usrResources=new ArrayList<>();

        for(UsrDto usrDto:usrListDto.getUsrDtos()){
            usrResources.add(usrDto.toResource());
        }

        //그리드에 맞는 형식으로 전환
        JSONObject result =new JSONObject();
        result.put("result",true);

        JSONObject data = new JSONObject();
        data.put("contents", usrResources);

        JSONObject pagination = new JSONObject();
        pagination.put("page", page);
        pagination.put("totalCount", usrListDto.getTotalCount());

        data.put("pagination", pagination);
        result.put("data", data);

        return new ResponseEntity(result, HttpStatus.OK);
    }

    @ApiOperation(value="고객 상세 조회")
    @GetMapping("/customer/detail")
    @ResponseBody
    public ResponseEntity detailCustomer(@RequestParam String usrId){

        UsrDto usrDto = customerManagementService.detailCustomer(usrId);
        UsrAuthDto usrAuthDto= customerManagementService.getCustomerAuth(usrId);

        UsrResource usrResource= new UsrResource();

        usrResource.usrDtoToResource(usrDto);
        usrResource.authDtoToResource(usrAuthDto);

        return new ResponseEntity(usrResource, HttpStatus.OK);
    }

    @ApiOperation(value="고객 삭제")
    @DeleteMapping("/customer")
    public ResponseEntity deleteCustomer(@RequestParam String usrId){
        //customerManagementService.deleteCustomer(usrId);
        customerManagementService.changeCustomerUse(usrId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value="고객 리스트 삭제")
    @DeleteMapping("/customer/list")
    public ResponseEntity deleteCustomerList(@RequestParam String usrIds){

        //그리드에서 가져온 정보를 잘라서 사용.
        StringTokenizer tk=new StringTokenizer(usrIds,",");
        while (tk.hasMoreElements()){
            try{
                String usr=tk.nextToken();
                customerManagementService.changeCustomerUse(usr);
                //customerManagementService.deleteCustomer(usr);
                //customerManagementService.deleteCustomerAuth(usr);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value="고객 수정 - 상세")
    @PutMapping("/customer/detail")
    public ResponseEntity updateCustomer(@Valid @RequestBody UsrResource usrResource){

        customerManagementService.updateCustomer(usrResource.toDto());
        customerManagementService.updateCustomerAuth(usrResource.toCustAuthDto());
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value="고객 수정")
    @PutMapping("/customer")
    public ResponseEntity updateCustomer(@Valid @RequestBody JSONObject body) {
        JSONObject result = new JSONObject();

        try {
            //수정 데이터를 하나씩 parse해서 보낸다.
            ArrayList<Map<String, Object>> arrayList = (ArrayList<Map<String, Object>>) body.get("updatedRows");
            int len = arrayList.size();

            for (int i = 0; i < len; i++) {
                customerManagementService.updateCustomer(UsrDto.builder()
                        .usrId(arrayList.get(i).get("usrId").toString())
                        .nm(arrayList.get(i).get("nm").toString())
                        .useYn(Boolean.parseBoolean(arrayList.get(i).get("useYn").toString()))
                        .build());

                result.put("result", true);
                result.put("data", null);
            }

        } catch (Exception e) {
            result.put("result", false);
            result.put("data", null);

        } finally {
            return new ResponseEntity(result, HttpStatus.OK);
        }
    }
}
