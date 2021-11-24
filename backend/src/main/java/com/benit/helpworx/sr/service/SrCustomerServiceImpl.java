package com.benit.helpworx.sr.service;

import com.benit.helpworx.common.entity.JobGb;
import com.benit.helpworx.common.entity.Sr;
import com.benit.helpworx.common.entity.SrRcptTreat;
import com.benit.helpworx.common.repository.*;
import com.benit.helpworx.sr.dto.SrDto;
import com.benit.helpworx.sr.dto.SrProgressDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SrCustomerServiceImpl implements SrCustomerService{

    @Autowired
    private SrRepository srRepository;

    @Autowired
    private SrRepositorySupport srRepositorySupport;

    @Autowired
    private SrRcptTreatRepository srRcptTreatRepository;

    @Autowired
    private SysRepository sysRepository;

    @Autowired
    private JobGbRepository jobGbRepository;

    /**
     * 고객 SR 목록 검색 Service + 페이징
     */
    @Override
    public List<SrDto> getSrs(int page, int perPage, SrDto dto) {
        List<Sr> result = srRepositorySupport.findSrs(page, perPage, dto);
        List<SrDto> dtoResults = new ArrayList<>();
        result.forEach(e -> dtoResults.add(e.toDto()));
        return dtoResults;
    }

    @Override
    public int insertSr(SrDto dto) {
        srRepository.save(dto.toEntity());
        int res = srRepository.findByUsrIdAndRegTim(dto.getUsrId(), dto.getRegTim()).getSrId();
        return res;
    }

    @Override
    public SrDto findBySrId(int srId) {
        Sr result = srRepository.findById(srId).orElseThrow(() -> new NoSuchElementException("해당 SR이 없습니다. id = "+srId));
        return result.toDto();
    }

    @Transactional
    @Override
    public void updateSr(SrDto dto) {
        Sr target = srRepository.findById(dto.getSrId())
                .orElseThrow(() -> new NoSuchElementException("해당 SR이 없습니다. id = "+dto.getSrId()));
        target.update(dto.getSysCd(), dto.getJobGbId(), dto.getReqGb(), dto.getSbjct(), dto.getDscr(), dto.getRegTim());
    }

    @Override
    public void deleteSr(int srId) {
        srRepository.deleteById(srId);
    }

    @Override
    public SrProgressDto findSrProgressBySrId(int srId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Sr sr = srRepository.findBySrId(srId).orElse(null);
        SrRcptTreat srRcptTreat = srRcptTreatRepository.findBySrId(srId).orElse(null);

        String rcptState = "";
        String doState = "";
        String treatState = "";
        String rqmtTim = null;
        String recivrId = null;
        String opetrId = null;
        boolean returnState = false;

        switch (sr.getState()){
            case "반려":
                doState=null;
                treatState=null;
                rcptState=null;
                returnState =true;
            case "대기":
                doState=null;
                treatState=null;
                break;
            case "진행중":
                rcptState="접수 완료";
                doState="진행 중";
                treatState=null;
                recivrId = srRcptTreat.getRecivrId();
                break;
            case "해결":
                rcptState="접수 완료";
                doState="진행 완료";
                treatState="처리 완료";
                rqmtTim = srRcptTreat.getRqmtTim();
                opetrId = srRcptTreat.getOpetrId();
                break;
        }
        String regTim=null;
        if(sr.getRegTim()!=null) regTim = sdf.format(sr.getRegTim());
        String rcptDate=null;
        if(srRcptTreat!=null && srRcptTreat.getCmplPrdt()!=null) rcptDate = sdf.format(srRcptTreat.getTreatBeginTim());
        String treatTim=null;
        if(srRcptTreat!=null && srRcptTreat.getTreatEndTim()!=null) treatTim = sdf.format(srRcptTreat.getTreatEndTim());

        SrProgressDto ret = new SrProgressDto().builder()
                .srId(srId)
                .regId(sr.getUsrId())
                .regState("등록 완료")
                .regTim(regTim)
                .recivrId(recivrId)
                .rcptState(rcptState)
                .rcptDate(rcptDate)
                .opetrId(opetrId)
                .treatState(treatState)
                .treatTim(treatTim)
                .doId(opetrId)
                .doState(doState)
                .doTim(rqmtTim)
                .returnState(returnState)
                .build();

        return ret;
    }

    @Override
    public String findSysNmBySysCd(int sysCd) {
        String ret = sysRepository.findById(sysCd).orElse(null).getSysNm();
        return ret;
    }

    @Override
    public String findJobNmByGobGbId(int jobGbId) {
        String ret = jobGbRepository.findById(jobGbId).orElse(null).getJob();
        return ret;
    }

    @Override
    public List<JobGb> getJobGbBySysCd(int sysCd) {
        List<JobGb> ret = jobGbRepository.findAllBySysCd(sysCd);

        return ret;
    }

    @Override
    public long getSrListCount(SrDto dto) {
        long count = srRepositorySupport.getSrListCount(dto);
        return count;
    }

    /**
     * 해당 srId에 해당하는 sr 존재 여부 조회
     *
     * @param srId
     * @return
     */
    @Override
    public boolean isExistSr(int srId) {
        return srRepository.existsBySrId(srId);
    }


}
