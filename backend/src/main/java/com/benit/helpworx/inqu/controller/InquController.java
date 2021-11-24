package com.benit.helpworx.inqu.controller;

import com.benit.helpworx.common.entity.InquLog;
import com.benit.helpworx.common.entity.RltmInqu;
import com.benit.helpworx.common.jwt.JwtTokenUtil;
import com.benit.helpworx.common.repository.UsrRepository;
import com.benit.helpworx.custmng.service.CustomerManagementService;
import com.benit.helpworx.inqu.dto.InquLogDto;
import com.benit.helpworx.inqu.dto.LogListDto;
import com.benit.helpworx.inqu.dto.RltmInquDto;
import com.benit.helpworx.inqu.dto.SysDto;
import com.benit.helpworx.inqu.resource.InquLogResource;
import com.benit.helpworx.inqu.resource.LogListResource;
import com.benit.helpworx.inqu.service.InquService;
import com.benit.helpworx.inqu.service.OperatorInquService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("")
public class InquController {
    @Autowired
    InquService inquService;

    @Autowired
    CustomerManagementService customerManagementService;

    @Autowired
    OperatorInquService operatorInquService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 새로운 문의 시작
     * @param dto
     * @return
     */
    @ApiOperation(value = "새로운 문의 시작")
    @PostMapping("/inqu/create")
    public ResponseEntity createInqu(@RequestBody RltmInquDto dto){
        int inquId = inquService.createInqu(dto);
        return new ResponseEntity(inquId, HttpStatus.OK);
    }

    /**
     * inquId를 가지고 그 문의의 문의자 찾기 (채팅창 재로딩을 위함)
     * @param inquId
     * @return
     */
    @ApiOperation(value = "문의자 ID 찾기")
    @GetMapping("/inqu/find/inquUsrId")
    public ResponseEntity findInquUsrId(@RequestParam int inquId){
        String ret = inquService.findInquUsrId(inquId);
        return new ResponseEntity(ret, HttpStatus.OK);
    }

    /**
     * 문의 내용 inqu_log DB에 저장
     * @param dto
     * @return
     */
    @ApiOperation(value = "보낸 메시지 내용 log에 저장")
    @PostMapping("/inqu/log/save")
    public ResponseEntity saveInquLog(@RequestBody InquLogDto dto){
        inquService.saveInquLog(dto);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * usr의 모든 채팅 목록과 마지막 채팅 내역 불러오기
     * 채팅창 리스트 생성을 위함 - inqu lobby
     * @param usrId
     * @return
     */
    @ApiOperation(value = "usr의 모든 채팅 목록과 마지막 채팅 내역 불러오기")
    @GetMapping("/inqu/find/all")
    public ResponseEntity findAllListByUsrId(@RequestParam String usrId){
        List<RltmInqu> list = inquService.findAllListByUsrId(usrId);
        List<LogListResource> ret = new ArrayList<>();
        for (int idx = 0; idx < list.size(); idx++) {
            InquLog inquLog = inquService.getLastLogByInquId(list.get(idx).getInquId());
            // 채팅방이 생성된 후 대화를 시작하지 않고 종료했다면 채팅방 삭제
            int inquId = list.get(idx).getInquId();
            if(inquLog==null) {
                list.remove(idx);
                idx--;
                inquService.deleteInqu(inquId);
            } else {
                int sysCd = list.get(idx).getSysCd();
                String sysNm;
                if(sysCd == 1) sysNm = "PeopleWorx";
                else if(sysCd == 2) sysNm = "SalesWorx";
                else if(sysCd == 3) sysNm = "r-PLANNA";
                else sysNm = "r-CoCoAna";
                ret.add(new LogListResource(inquId, sysNm, inquLog.getInquDscr(), calcDate(inquLog.getTim()),
                        inquLog.getTim(), operatorInquService.cntUncnfrmLogByInquId(inquId, usrId)));
            }
        }
        if(ret.isEmpty()){
            ret = null;
        } else {
            ret.sort((o1, o2) ->  o2.getTim().compareTo(o1.getTim()));
        }
        return new ResponseEntity(ret, HttpStatus.OK);
    }

    /**
     * 날짜 계산
     * 오늘 채팅: a KK:mm
     * 하루 전 채팅: 어제
     * 올해 채팅: MM-dd
     * 올해 이전 채팅: yyyy-MM-dd
     * @param tim
     * @return
     */
    private String calcDate(Date tim) {
        String ret = null;
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfTime = new SimpleDateFormat("a KK:mm");
        SimpleDateFormat sdfYear = new SimpleDateFormat("MM-dd");


        // 오늘 날짜
        Date date = new Date();

        // 어제 날짜
        Date bDate = new Date();
        bDate = new Date(bDate.getTime() - (1000*60*60*24-1));

        // 올해
        Calendar calDate = Calendar.getInstance();
        calDate.setTime(date);
        int thisYear = calDate.get(Calendar.YEAR);

        Calendar calTim = Calendar.getInstance();
        calTim.setTime(date);
        int timYear = calDate.get(Calendar.YEAR);

        if(sdfDate.format(date).equals(sdfDate.format(tim))) {
            ret = sdfTime.format(tim);
        } else if(sdfDate.format(bDate).equals(sdfDate.format(tim))) {
            ret = "어제";
        } else if(thisYear == timYear){
            ret = sdfYear.format(tim);
        } else {
            ret = sdfDate.format(tim);
        }

        return ret;
    }

    /**
     * 이전 대화 불러오기
     * 채팅창 닫은 후 이어하는 경우를 위함
     * @param inquId
     * @return
     */
    @ApiOperation(value = "이전 대화 목록 불러오기")
    @GetMapping("/inqu/bef")
    public ResponseEntity getBeforeChat(@RequestParam int inquId){
        List<InquLogDto> list = inquService.findBefChatByInquId(inquId);
        SimpleDateFormat sdf = new SimpleDateFormat("kk:mm");
        List<InquLogResource> resList = new ArrayList<>();
        for (int idx = 0; idx < list.size(); idx++) {
            InquLogResource res = list.get(idx).toResource();
            res.setUsrNm(customerManagementService.detailCustomer(list.get(idx).getSndr()).getNm());
            res.setTim(sdf.format(list.get(idx).getTim()));
            resList.add(res);
        }
        // 시간순으로 정렬
        resList.sort((o1, o2) ->  o1.getTimD().compareTo(o2.getTimD()));

        HashMap<String, Object> ret = new HashMap<>();

        // 문의 종료 여부
        String stateStr = inquService.findInquByInquId(inquId).getState();
        boolean iscmpl = false;
        if(stateStr.equals("완료")) iscmpl = true;

        ret.put("state", iscmpl);
        ret.put("list", resList);
        return new ResponseEntity(ret, HttpStatus.OK);
    }

    /**
     * 문의 종료 후 db에 종료 저장
     * @param inquId
     * @return
     */
    @ApiOperation(value = "문의 종료")
    @PutMapping("/inqu/fin")
    public ResponseEntity finInqu(@RequestBody int inquId){
        inquService.finInqu(inquId);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 문의 삭제
     * @param inquId
     * @return
     */
    @ApiOperation(value = "채팅방 삭제")
    @DeleteMapping("/inqu")
    public ResponseEntity deleteInqu(@RequestBody int inquId){
        inquService.deleteInqu(inquId);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 문의 시작 전 어느 시스템에 대해 문의할 지 고르기 위해
     * 회사에서 사용하는 시스템 정보 가져오기
     * @param coCd
     * @return
     */
    @ApiOperation(value = "회사에서 사용하는 시스템 정보 가져오기")
    @GetMapping("/sys/using")
    public ResponseEntity getUsingSysList(@RequestParam String coCd){
        List<SysDto> list = inquService.getUsingSysList(coCd);

        return new ResponseEntity(list, HttpStatus.OK);
    }

    @ApiOperation(value = "문의 대화 읽음 처리")
    @PutMapping("/inqu/log/cnfrm")
    public ResponseEntity putLogStateCnfrmByInquId(@RequestBody int inquId){
        inquService.putLogStateCnfrmByInquId(inquId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "유저 - 확인 안한 문의 로그 개수")
    @GetMapping("/inqu/log/uncnfrm/cnt")
    public ResponseEntity cntUnCnfrmByUsrId(@RequestHeader final HttpHeaders header){
        String accessToken= jwtTokenUtil.resolveToken(header);
        String usrId=jwtTokenUtil.extractUsrIdAccessToken(accessToken);
        int cnt = inquService.cntUnCnfrmByUsrId(usrId);
        return new ResponseEntity(cnt, HttpStatus.OK);
    }
    @ApiOperation(value = "운영자 - 확인 안한 문의 로그 개수")
    @GetMapping("/inqu/oper/log/uncnfrm/cnt")
    public ResponseEntity cntOperUnCnfrmByUsrId(@RequestHeader final HttpHeaders header){
        String accessToken= jwtTokenUtil.resolveToken(header);
        String usrId=jwtTokenUtil.extractUsrIdAccessToken(accessToken);
        int cnt = inquService.cntOperUnCnfrmByUsrId(usrId);
        return new ResponseEntity(cnt, HttpStatus.OK);
    }
}
