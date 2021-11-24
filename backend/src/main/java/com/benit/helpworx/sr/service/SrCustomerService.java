package com.benit.helpworx.sr.service;

import com.benit.helpworx.common.entity.JobGb;
import com.benit.helpworx.sr.dto.SrDto;
import com.benit.helpworx.sr.dto.SrProgressDto;

import java.util.List;

public interface SrCustomerService {

    /**
     * 고객 SR 목록 검색 Service + 페이징
     */
    public List<SrDto> getSrs(int page, int perPage, SrDto dto);

    /**
     * 고객 SR 등록
     * @return
     */
    public int insertSr(SrDto dto);

    /**
     * 고객 SR 상세 확인
     */
    public SrDto findBySrId(int srId);

    /**
     * 고객 SR 수정
     */
    public void updateSr(SrDto dto);

    /**
     * 고객 SR 삭제
     */
    public void deleteSr(int srId);

    /**
     * SR의 진행 단계 조회
     */
    public SrProgressDto findSrProgressBySrId(int srId);

    /**
     * Syscd로 SysNm 반환
     */
    public String findSysNmBySysCd(int sysCd);

    /**
     * jobGbId로 이름 반환
     */
    public String findJobNmByGobGbId(int jobGbId);

    /**
     * SysCd로 JobGb 반환
     */
    public List<JobGb> getJobGbBySysCd(int sysCd);

    /**
     * 검색 내용의 총 건수 조회
     *
     * @param dto
     * @return
     */
    public long getSrListCount(SrDto dto);

    /**
     * 해당 srId에 해당하는 sr 존재 여부 조회
     *
     * @param srId
     * @return
     */
    public boolean isExistSr(int srId);
}
