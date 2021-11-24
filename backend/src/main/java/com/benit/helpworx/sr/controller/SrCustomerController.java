package com.benit.helpworx.sr.controller;

import com.benit.helpworx.common.entity.JobGb;
import com.benit.helpworx.common.jwt.JwtTokenUtil;
import com.benit.helpworx.sr.dto.SrDto;
import com.benit.helpworx.sr.dto.SrProgressDto;
import com.benit.helpworx.sr.resource.SrDetailResrce;
import com.benit.helpworx.sr.service.SrCustomerService;
import io.swagger.annotations.ApiOperation;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("")
public class SrCustomerController {

    @Autowired
    private SrCustomerService srCustomerService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 고객 SR 목록 조회 API
     * 페이징 + tui-grid에 맞는 형식으로 return
     *
     * @param page
     * @param perPage
     * @param coCd
     * @param sysCd
     * @param jobGbId
     * @param usrId
     * @param reqGb
     * @param state
     * @param sbjct
     * @param dscr
     * @return
     */
    @ApiOperation(value ="고객 SR 조회")
    @GetMapping("/srs")
    public ResponseEntity getSrsJson(@RequestParam int page,
                                     @RequestParam int perPage,
                                     @RequestParam String coCd,
                                     @RequestParam int sysCd,
                                     @RequestParam(required = false, defaultValue = "0") int jobGbId,
                                     @RequestParam(required = false) String usrId,
                                     @RequestParam(required = false) String reqGb,
                                     @RequestParam(required = false) String state,
                                     @RequestParam(required = false) String sbjct,
                                     @RequestParam(required = false) String dscr) {

        SrDto searchOptions = SrDto.builder()
                .coCd(coCd)
                .sysCd(sysCd)
                .jobGbId(jobGbId)
                .usrId(usrId)
                .reqGb(reqGb)
                .state(state)
                .sbjct(sbjct)
                .dscr(dscr)
                .build();
        List<SrDto> searchData = srCustomerService.getSrs(page, perPage, searchOptions);

        long count = srCustomerService.getSrListCount(searchOptions);

        // json 만들기 tui-grid에 맞는 형식으로 response하기 위함
        JSONObject result = new JSONObject();
        result.put("result", true);

        JSONObject data = new JSONObject();
        data.put("contents", searchData);

        JSONObject pagination = new JSONObject();
        pagination.put("page", page);
        pagination.put("totalCount", count);

        data.put("pagination", pagination);
        result.put("data", data);

        return new ResponseEntity(result, HttpStatus.OK);
    }

    /**
     * SR ID를 통해 특정 SR 상세 조회
     *
     * @param srId
     * @return
     */
    @ApiOperation(value = "고객 SR 상세 조회")
    @GetMapping("/sr/{srId}")
    public ResponseEntity findSrById(@RequestParam int srId) {
        SrDto srDto = srCustomerService.findBySrId(srId);

        String sysNm = srCustomerService.findSysNmBySysCd(srDto.getSysCd());
        String jobNm = srCustomerService.findJobNmByGobGbId(srDto.getJobGbId());

        SrDetailResrce result = new SrDetailResrce().builder()
                .coCd(srDto.getCoCd())
                .sysNm(sysNm)
                .jobGb(jobNm)
                .reqGb(srDto.getReqGb())
                .sbjct(srDto.getSbjct())
                .dscr(srDto.getDscr())
                .regTim(srDto.getRegTim())
                .writer(srDto.getUsrId())
                .build();

        return new ResponseEntity(result, HttpStatus.OK);
    }

    @ApiOperation(value = "고객 SR 상세 조회 - 진행 정도 확인")
    @GetMapping("/sr/progress/srId")
    public ResponseEntity findSrProgressById(@RequestParam int srId) {

        SrProgressDto result = srCustomerService.findSrProgressBySrId(srId);

        return new ResponseEntity(result, HttpStatus.OK);
    }

    @ApiOperation(value = "고객 SR 등록")
    @PostMapping("/sr")
    public ResponseEntity saveSr(@RequestBody SrDto srDto) {
        int srId = srCustomerService.insertSr(srDto);
        return new ResponseEntity(srId, HttpStatus.OK);
    }

    @ApiOperation(value = "고객의 SR 수정")
    @PutMapping("/sr")
    public ResponseEntity updateSr(@RequestBody SrDto srDto) {
        System.out.println(srDto.toString());
        srCustomerService.updateSr(srDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "고객의 SR 삭제")
    @DeleteMapping("/sr")
    public ResponseEntity deleteSr(@RequestHeader HttpHeaders header, @RequestParam int srId) {
        // header에 저장된 현재 usrId 정보 받아오기
        String accessToken= jwtTokenUtil.resolveToken(header);
        String usrId=jwtTokenUtil.extractUsrIdAccessToken(accessToken);

        // 삭제 권한 확인
        checkDeleteSr(srId, usrId);
        srCustomerService.deleteSr(srId);

        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 고객이 SR 체크박스로 다건 삭제할 때
     * parameter로 받는 string을 integer list로 바꿔서 service 단에 전달
     *
     * @param srIds 삭제할 srId들의 리스트를 string type 으로 받는다.
     * @return
     */
    @ApiOperation(value = "고객의 SR 리스트 삭제")
    @DeleteMapping("/sr/list")
    public ResponseEntity deleteSrs(@RequestHeader HttpHeaders header, @RequestParam String srIds) {
        // header에 저장된 현재 usrId 정보 받아오기
        String accessToken= jwtTokenUtil.resolveToken(header);
        String usrId=jwtTokenUtil.extractUsrIdAccessToken(accessToken);

        // list 형태의 String으로 받은 srIds를 Integer 로 변환 후 삭제 가능 여부 확인
        StringTokenizer st = new StringTokenizer(srIds,",");
        ArrayList<Integer> srIdList = new ArrayList<>();
        while(st.hasMoreElements()) {
            int id = Integer.parseInt(st.nextToken());
            srIdList.add(id);
            
            // 삭제 권한 확인
            checkDeleteSr(id, usrId);
        }

        int maxSize = srIdList.size();
        for(int index=0; index<maxSize; index++) {
            int id = srIdList.get(index);
            srCustomerService.deleteSr(id);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "시스템 목록별 업무 조회")
    @GetMapping("/jobGb/sys")
    public ResponseEntity getJobGbBySysCd(@RequestParam int sysCd){
        List<JobGb> ret = srCustomerService.getJobGbBySysCd(sysCd);
        return new ResponseEntity(ret, HttpStatus.OK);
    }

    @ApiOperation(value = "SR 조회 건수")
    @GetMapping("/sr/count")
    public ResponseEntity selectSrListCnt(@RequestParam String coCd,
                                          @RequestParam int sysCd,
                                          @RequestParam(required = false, defaultValue = "0") int jobGbId,
                                          @RequestParam(required = false) String usrId,
                                          @RequestParam(required = false) String reqGb,
                                          @RequestParam(required = false) String state,
                                          @RequestParam(required = false) String sbjct,
                                          @RequestParam(required = false) String dscr) {

        SrDto searchOptions = SrDto.builder()
                .coCd(coCd)
                .sysCd(sysCd)
                .jobGbId(jobGbId)
                .usrId(usrId)
                .reqGb(reqGb)
                .state(state)
                .sbjct(sbjct)
                .dscr(dscr)
                .build();

        long count = srCustomerService.getSrListCount(searchOptions);

        return new ResponseEntity(count, HttpStatus.OK);
    }

    /**
     * 고객이 sr을 삭제 가능한지 확인 후 예외처리
     * 
     * @param srId
     * @param usrId
     */
    private void checkDeleteSr(int srId, String usrId) {
        SrDto sr = null;

        if(srCustomerService.isExistSr(srId)) {
            sr = srCustomerService.findBySrId(srId);
        }else {
            throw new NoSuchElementException("해당 SR이 존재하지 않습니다. SR ID : "+srId);
        }

        // 대기 상태가 아닌 경우 삭제 권한 없음
        if(!StringUtils.equals("대기", sr.getState())) {
            throw new AccessDeniedException("삭제 권한이 없습니다. SR ID : "+srId);
        }

        // 해당 사용자가 작성한 sr인지 권한 확인
        if(!StringUtils.equals(sr.getUsrId(), usrId)) {
            throw new AccessDeniedException("삭제 권한이 없습니다. SR ID : "+srId);
        }
    }

}