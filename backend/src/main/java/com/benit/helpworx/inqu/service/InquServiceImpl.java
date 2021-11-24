package com.benit.helpworx.inqu.service;

import com.benit.helpworx.common.entity.Co;
import com.benit.helpworx.common.entity.InquLog;
import com.benit.helpworx.common.entity.RltmInqu;
import com.benit.helpworx.common.entity.Sys;
import com.benit.helpworx.common.repository.CoRepository;
import com.benit.helpworx.common.repository.InquLogRepository;
import com.benit.helpworx.common.repository.RltmInquRepository;
import com.benit.helpworx.inqu.dto.InquLogDto;
import com.benit.helpworx.inqu.dto.RltmInquDto;
import com.benit.helpworx.inqu.dto.SysDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class InquServiceImpl implements InquService {
    @Autowired
    RltmInquRepository rltmInquRepository;

    @Autowired
    InquLogRepository inquLogRepository;

    @Autowired
    CoRepository coRepository;

    /**
     * 새로운 문의 생성, DB에 저장 후 inquId 리턴
     * @param dto
     * @return
     */
    @Override
    public int createInqu(RltmInquDto dto) {
        RltmInqu rltmInqu = new RltmInqu().builder()
                .sysCd(dto.getSysCd())
                .usrId(dto.getUsrId())
                .state(dto.getState())
                .beginTim(dto.getBeginTim())
                .coCd(dto.getCoCd())
                .sbjct(dto.getSbjct())
                .build();
        rltmInquRepository.save(rltmInqu);

        // 새로 생성된 inquId를 같이 리턴
        int inquId = rltmInquRepository.findOneByUsrIdAndSysCdAndBeginTim(dto.getUsrId(),
                dto.getSysCd(), dto.getBeginTim()).getInquId();

        // 회사의 운영자들에게 알람



        return inquId;
    }

    /**
     * inquId로 문의자 usrId 찾기
     * @param inquId
     * @return
     */
    @Override
    public String findInquUsrId(int inquId) {
        String usrId = rltmInquRepository.findById(inquId).orElse(null).getUsrId();
        return usrId;
    }

    /**
     * inqu log DB에 저장
     * @param dto
     */
    @Override
    public void saveInquLog(InquLogDto dto) {
        InquLog inquLog = dto.toEntity();
        inquLogRepository.save(inquLog);
        return;
    }

    /**
     * usrId가 문의한 모든 문의 내역 리턴
     * @param usrId
     * @return
     */
    @Override
    public List<RltmInqu> findAllListByUsrId(String usrId) {
        List<RltmInqu> list = rltmInquRepository.findAllListByUsrId(usrId);
        return list;
    }

    /**
     * inquId의 모든 문의 log 리턴
     * @param inquId
     * @return
     */
    @Override
    public List<InquLogDto> findBefChatByInquId(int inquId) {
        List<InquLog> list = inquLogRepository.findAllByInquId(inquId);
        List<InquLogDto> ret = new ArrayList<>();
        for (int idx = 0; idx < list.size(); idx++) {
            ret.add(list.get(idx).toDto());
        }
        return ret;
    }

    /**
     * 문의 log 조회 후 inquLogId 리턴
     * @param inquId
     * @param inquDscr
     * @param tim
     * @return
     */
    @Override
    public int findOneByInquIdAndInquDscrAndTim(int inquId, String inquDscr, Date tim) {
        int inquLogId = inquLogRepository.findOneByInquIdAndInquDscrAndTim(inquId, inquDscr, tim).getInquLogId();

        return inquLogId;
    }

    /**
     * 해당 문의의 마지막 채팅 log 리턴
     * @param inquId
     * @return
     */
    @Override
    public InquLog getLastLogByInquId(int inquId) {
         InquLog ret = inquLogRepository.findTop1ByInquIdOrderByTimDesc(inquId).orElse(null);
        return ret;
    }

    /**
     * 문의 삭제
     * @param inquId
     */
    @Override
    public void deleteInqu(int inquId) {
        rltmInquRepository.deleteById(inquId);
    }

    /**
     * 회사가 사용중인 시스템 리턴
     * @param coCd
     * @return
     */
    @Override
    public List<SysDto> getUsingSysList(String coCd) {
        List<SysDto> list = new ArrayList<>();
        Co co = coRepository.findById(coCd).orElse(null);
        if(co.isPwxUseYn()) list.add(new SysDto(1,"PeopleWorx"));
        if(co.isSwxUseYn()) list.add(new SysDto(2, "SalesWorx"));
        if(co.isRplUseYn()) list.add(new SysDto(3, "r-PLANNA"));
        if(co.isRcoUseYn()) list.add(new SysDto(4, "r-CoCoAna"));
        return list;
    }

    /**
     * inquId로 문의 리턴
     * @param inquId
     * @return
     */
    @Override
    public RltmInqu findInquByInquId(int inquId) {
        return rltmInquRepository.findById(inquId).orElse(null);
    }

    /**
     * 문의 종료
     * @param inquId
     */
    @Override
    public void finInqu(int inquId) {
        RltmInqu rltmInqu = rltmInquRepository.findById(inquId).orElse(null);
        rltmInqu.setEndTim(new Date());
        rltmInqu.setState("완료");
        rltmInquRepository.save(rltmInqu);
    }

    /**
     * 문의 log 읽음 처리
     * @param inquId
     */
    @Override
    public void putLogStateCnfrmByInquId(int inquId) {
        List<InquLog> logs = inquLogRepository.findAllByInquId(inquId);
        logs.forEach(e -> {
            e.setState(true);
            inquLogRepository.save(e);
        });
    }

    /**
     * 유저의 미확인 문의 개수 리턴
     * @param usrId
     * @return
     */
    @Override
    public int cntUnCnfrmByUsrId(String usrId) {
        List<RltmInqu> list = rltmInquRepository.findAllByUsrIdAndState(usrId,"진행중");
        int cnt = 0;
        for (int idx = 0; idx < list.size(); idx++) {
            cnt += inquLogRepository.countByInquIdAndStateAndSndrNot(list.get(idx).getInquId(), false, usrId);
        }
        return cnt;
    }

    /**
     * 운영자의 미확인 문의 개수 리턴
     * @param usrId
     * @return
     */
    @Override
    public int cntOperUnCnfrmByUsrId(String usrId) {
        List<RltmInqu> list = rltmInquRepository.findAllByAnswrrAndState(usrId,"진행중");
        int cnt = 0;
        for (int idx = 0; idx < list.size(); idx++) {
            cnt += inquLogRepository.countByInquIdAndStateAndSndrNot(list.get(idx).getInquId(), false, usrId);
        }
        return cnt;
    }
}
