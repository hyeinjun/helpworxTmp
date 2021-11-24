package com.benit.helpworx.sr.controller;

import com.benit.helpworx.common.entity.SrRjt;
import com.benit.helpworx.common.jwt.JwtTokenUtil;
import com.benit.helpworx.sr.dto.*;
import com.benit.helpworx.sr.resource.*;
import com.benit.helpworx.sr.service.SrCustomerService;
import com.benit.helpworx.sr.service.SrOperatorService;
import io.swagger.annotations.ApiOperation;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@CrossOrigin(origins = { "*" }, allowCredentials = "true")
@RestController
@RequestMapping("")
public class SrOperatorController {
    @Autowired
    SrOperatorService srOperatorService;

    @Autowired
    SrCustomerService srCustomerService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @ApiOperation(value = "SR접수 - 저장")
    @PostMapping("/sr/rcpt")
    public ResponseEntity saveSrRcpt(@RequestBody SrRcptDto dto){
        // 입력받은 내용을 저장하기 위한 service 호출
        srOperatorService.saveSrRcpt(dto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "SR처리 - 저장")
    @PostMapping("/sr/treat")
    public ResponseEntity saveSrTreat(@RequestBody SrTreatDto dto){
        // 입력받은 내용을 저장하기 위한 service 호출
        srOperatorService.saveSrTreat(dto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "SR접수 - 조회")
    @GetMapping("/sr/rcpt")
    public ResponseEntity getSrRcpt(@RequestParam int srId){
        SrRcptResource ret = new SrRcptResource();
        // Sr 접수, 처리 조회
        SrRcptTreatDto srRcptTreatDto = srOperatorService.getSrRcptTreatWithSrId(srId);
        // dto -> resource
        if(srRcptTreatDto==null) return new ResponseEntity(null, HttpStatus.OK);

        ret.setRecivrId(srRcptTreatDto.getRecivrId());
        ret.setRcptDscr(srRcptTreatDto.getRcptDscr());
        ret.setCmplPrdt(srRcptTreatDto.getCmplPrdt());

        return new ResponseEntity(ret, HttpStatus.OK);
    }

    @ApiOperation(value = "SR처리 - 조회")
    @GetMapping("/sr/treat")
    public ResponseEntity getSrTreat(@RequestParam int srId){
        SrRcptTreatResrce ret = new SrRcptTreatResrce();
        // Sr 조회
        SrDto srDto = srOperatorService.getSrWithSrId(srId);

        // Sr 접수, 처리 조회
        SrRcptTreatDto srRcptTreatDto = srOperatorService.getSrRcptTreatWithSrId(srId);


        if(srRcptTreatDto==null) return new ResponseEntity(ret, HttpStatus.OK);
        ret.setOpetrId(srRcptTreatDto.getOpetrId());
        ret.setTreatDscr(srRcptTreatDto.getTreatDscr());
        ret.setTreatBeginTim(srRcptTreatDto.getTreatBeginTim());
        ret.setTreatEndTim(srRcptTreatDto.getTreatEndTim());
        ret.setRqmtTim(srRcptTreatDto.getRqmtTim());

        return new ResponseEntity(ret, HttpStatus.OK);
    }

    @ApiOperation(value = "SR 접수 - 삭제")
    @DeleteMapping("/sr/rcpt")
    public ResponseEntity deleteSrRcpt(@RequestParam int srId){
        srOperatorService.deleteSrRcpt(srId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "SR 처리 - 삭제")
    @DeleteMapping("/sr/treat")
    public ResponseEntity deleteSrTreat(@RequestParam int srId){
        srOperatorService.deleteSrTreat(srId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "SR, SR접수, SR처리 - 반려")
    @PostMapping("/sr/reject")
    public ResponseEntity rejectSr(@RequestBody SrRjt srRjt){
        srOperatorService.rejectSr(srRjt);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "SR 반려 조회")
    @GetMapping("/sr/reject")
    public ResponseEntity getRejectSrBySrId(@RequestParam int srId){
        SrRjt ret = srOperatorService.getRejectSrBySrId(srId);
        return new ResponseEntity(ret, HttpStatus.OK);
    }


    @ApiOperation(value = "SR 반려 취소")
    @GetMapping("/sr/cancel/reject")
    public ResponseEntity cancelReturned(@RequestParam int srId){
        srOperatorService.cancelReturned(srId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "SR, SR접수, SR처리 다건 삭제")
    @DeleteMapping("/sr/rcpt-treat/list")
    public ResponseEntity deleteSrRcptTreatList(@RequestHeader HttpHeaders header, @RequestParam String srIds) {
        String accessToken= jwtTokenUtil.resolveToken(header);
        String usrId = jwtTokenUtil.extractUsrIdAccessToken(accessToken);
        String coCd = jwtTokenUtil.extractCoCd(accessToken);

        StringTokenizer st = new StringTokenizer(srIds,",");
        ArrayList<Integer> srIdList = new ArrayList<>();

        while(st.hasMoreElements()) {
            int id = Integer.parseInt(st.nextToken());
            srIdList.add(id);

            // 권한 확인
            checkReturnSr(id, coCd);
        }

        int maxSize = srIdList.size();
        for(int index=0; index<maxSize; index++) {
            int id = srIdList.get(index);
            SrRjt srRjt = SrRjt.builder()
                    .srId(id)
                    .rjtDscr("일괄 반려 처리되었습니다.")
                    .retrner(usrId) // 헤더에서 사용자 정보 받아오기
                    .rjtTim(new Date())
                    .build();
            srOperatorService.rejectSr(srRjt); // SrRcptTreat 존재 체크는 서비스 단에서 이루어짐
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 운영자의 SR 목록 검색 API
     * 기존 parameter에 tui-grid에 필요한 page,perPage 추가
     * @return
     */
    @ApiOperation(value = "SR 검색")
    @GetMapping("/sr-operator")
    public ResponseEntity findSrs(@RequestParam int page,
                                  @RequestParam int perPage,
                                  @RequestParam String coCd,
                                  @RequestParam int sysCd,
                                  @RequestParam(required = false, defaultValue = "0") int jobGbId,
                                  @RequestParam(required = false) String reqGb,
                                  @RequestParam(required = false) String state,
                                  @RequestParam(required = false) String sbjct,
                                  @RequestParam(required = false) String dscr) {

        SrDto searchOptions = SrDto.builder()
                .coCd(coCd)
                .sysCd(sysCd)
                .jobGbId(jobGbId)
                .reqGb(reqGb)
                .state(state)
                .sbjct(sbjct)
                .dscr(dscr)
                .build();

        List<SrSrRcptTreatDto> contents = srOperatorService.findSrs(page, perPage, searchOptions);
        long totalCount = srCustomerService.getSrListCount(searchOptions);

        JSONObject result = new JSONObject();
        result.put("result", true);

        JSONObject data = new JSONObject();
        data.put("contents", contents);

        JSONObject pagination = new JSONObject();
        pagination.put("page", page);
        pagination.put("totalCount", totalCount);

        data.put("pagination", pagination);
        result.put("data", data);

        return new ResponseEntity(result, HttpStatus.OK);
    }

    @ApiOperation("Sr 시스템 현황 차트를 그리기 위한 데이터")
    @GetMapping("sr/chart/count")
    public ResponseEntity srSystemStatusBarChart(@RequestParam int sysCd) {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime past = now.minusYears(1);

        String[] reqGbList = {"개선", "오류", "문의", "데이터수정"};

        List<SrStatusBarChartDataSetResource> resourceList = new ArrayList<>();

        Arrays.stream(reqGbList)
                .forEach(reqGb -> {
                    List<Long> countList = new ArrayList<>();

                    List<SrCntBySysAndReqDto> dto = srOperatorService.srSystemStatusBarChart(sysCd, reqGb, past, now);
                    dto.forEach(countData -> {
                        countList.add(countData.getCount());
                    });

                    // (reqGb: 라벨 이름(요청 구분 데이터), Long[회사코드의 오름차순으로 정렬된 count 데이터])
                    resourceList.add(
                            SrStatusBarChartDataSetResource
                                    .builder()
                                    .label(reqGb)
                                    .data(countList.toArray(new Long[] {0L}))
                                    .build()
                    );
                });

        return new ResponseEntity(resourceList, HttpStatus.OK);
    }

    @ApiOperation(value = "월별 SR 건수 현황을 보여주는 라인 그래프 데이터")
    @GetMapping("sr/chart/count/per-month")
    public ResponseEntity srCntPerMonthLineChart(@RequestParam String coCd
                                                ,@RequestParam int sysCd) {

        List<SrCntPerMonthDto> list = srOperatorService.srCntLineChart(coCd, sysCd);

        int index = list.size();
        String[] months = new String[index];
        long[] data = new long[index];
        for(int i=0;i<index;i++) {
            months[i] = list.get(i).getMonth();
            data[i] = list.get(i).getCount();
        }

        SrStatusLineChartResource resource = SrStatusLineChartResource.builder()
                                                                    .label(months)
                                                                    .data(data)
                                                                    .build();
        return new ResponseEntity(resource, HttpStatus.OK);
    }
    
    @ApiOperation(value = "월별 평균 접수시간, 평균 처리시간을 보여주는 라인 그래프 데이터")
    @GetMapping("sr/chart/avg/rqmt")
    public ResponseEntity srAvgRqmtTimeLineChart(@RequestParam String coCd
                                                ,@RequestParam int sysCd) {

        RqmtTimeLineChartResource resource = srOperatorService.srAvgTimeLineChart(coCd, sysCd);
        return new ResponseEntity(resource, HttpStatus.OK);
    }

    @ApiOperation(value = "SR 접수와 조회의 상태 반환")
    @GetMapping("/sr/state/rcpt-treat")
    public ResponseEntity getRcptTreatState(@RequestParam int srId){
        HashMap<String, Boolean> ret = new HashMap<>();
        // Sr 접수, 처리 조회
        SrRcptTreatDto srRcptTreatDto = srOperatorService.getSrRcptTreatWithSrId(srId);
        if(srOperatorService.getSrWithSrId(srId).getState().equals("반려")) {
            ret.put("isCmplRcpt", false);
            ret.put("isCmplTreat", false);
            ret.put("isReturned", true);
            return new ResponseEntity(ret, HttpStatus.OK);

        }
        ret.put("isReturned", false);
        // dto -> resource
        if(srRcptTreatDto==null) {
            ret.put("isCmplRcpt", false);
            ret.put("isCmplTreat", false);
        }
        else {
            if(srRcptTreatDto.getRecivrId()!=null) ret.put("isCmplRcpt", true);
            else ret.put("isCmplRcpt", false);
            if(srRcptTreatDto.getOpetrId()!=null && srRcptTreatDto.getTreatDscr()!=null) ret.put("isCmplTreat", true);
            else ret.put("isCmplTreat", false);
        }



        return new ResponseEntity(ret, HttpStatus.OK);
    }

    /**
     * 운영자가 sr을 반려 가능한지 확인 후 예외처리
     *
     * @param srId
     * @param coCd
     */
    private void checkReturnSr(int srId, String coCd) {
        SrDto sr = null;

        // 해당 sr 존재 확인
        if(srCustomerService.isExistSr(srId)) {
            sr = srCustomerService.findBySrId(srId);
        }else {
            throw new NoSuchElementException("해당 SR이 존재하지 않습니다. SR ID : "+srId);
        }

        // 소속 회사 sr 인지 확인
        if(!StringUtils.equals(coCd, sr.getCoCd())) {
            throw new AccessDeniedException("반려 권한이 없습니다. SR ID : "+srId);
        }

        // 이미 반려 상태인지 확인
        if(StringUtils.equals("반려", sr.getState())) {
            throw new IllegalStateException("이미 반려 상태입니다. SR ID : "+srId);
        }
    }

}
